package com.example.uptime.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class WebClientConfig {

    @Bean
    public WebClient monitoringWebClient(UptimeProperties props) {
        HttpClient http = HttpClient.create()
                .followRedirect(props.http.followRedirects)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.http.connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(props.http.readTimeoutMs))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(props.http.readTimeoutMs, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(http))
                .defaultHeader("User-Agent", props.http.userAgent)
                // małe odpowiedzi, nie potrzebujemy dużych buforów
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(64 * 1024))
                        .build())
                .build();
    }
}
