package com.example.uptime.core;

import com.example.uptime.domain.CheckStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HttpChecker {

    private final WebClient webClient;

    public record Outcome(CheckStatus status, int latencyMs, String errorMsg) {}

    public HttpChecker(WebClient monitoringWebClient) {
        this.webClient = monitoringWebClient;
    }

    public Outcome check(String url) {
        long start = System.nanoTime();
        try {
            int code = webClient.head()
                    .uri(url)
                    .exchangeToMono(resp -> Mono.just(resp.statusCode()))
                    .onErrorResume(e -> webClient.get().uri(url)
                            .exchangeToMono(resp -> Mono.just(resp.statusCode())))
                    .map(HttpStatusCode::value)
                    .block();

            int ms = (int) ((System.nanoTime() - start) / 1_000_000);
            if (code >= 200 && code < 400) {
                return new Outcome(CheckStatus.UP, ms, null);
            } else {
                return new Outcome(CheckStatus.DOWN, ms, "HTTP " + code);
            }
        } catch (Exception e) {
            int ms = (int) ((System.nanoTime() - start) / 1_000_000);
            return new Outcome(CheckStatus.DOWN, ms, e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
