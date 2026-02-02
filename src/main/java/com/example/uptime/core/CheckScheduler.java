package com.example.uptime.core;

import com.example.uptime.config.UptimeProperties;
import com.example.uptime.domain.CheckResult;
import com.example.uptime.domain.Target;
import com.example.uptime.repo.CheckResultRepository;
import com.example.uptime.repo.TargetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CheckScheduler {

    private static final Logger log = LoggerFactory.getLogger(CheckScheduler.class);

    private final TargetRepository targets;
    private final CheckResultRepository results;
    private final HttpChecker checker;
    private final UptimeProperties props;
    private final ExecutorService pool;

    public CheckScheduler(TargetRepository targets, CheckResultRepository results,
                          HttpChecker checker, UptimeProperties props) {
        this.targets = targets;
        this.results = results;
        this.checker = checker;
        this.props = props;
        this.pool = Executors.newFixedThreadPool(props.getScheduler().getParallelism());
    }

    @Scheduled(fixedDelayString = "#{${uptime.scheduler.tick-sec:5} * 1000}")
    public void tick() {
        List<Target> all = targets.findAll();
        OffsetDateTime now = OffsetDateTime.now();

        for (Target t : all) {
            if (Boolean.FALSE.equals(t.getEnabled())) continue;

            OffsetDateTime last = results.findTopByTargetOrderByCreatedAtDesc(t)
                    .map(CheckResult::getCreatedAt)
                    .orElse(OffsetDateTime.MIN);

            long secSinceLast = Duration.between(last, now).getSeconds();
            if (secSinceLast < t.getCheckEverySec()) continue;

            pool.submit(() -> checkOnceAndSave(t));
        }
    }

    @Transactional
    void checkOnceAndSave(Target t) {
        var outcome = checker.check(t.getUrl());

        CheckResult res = new CheckResult()
                .setTarget(t)
                .setStatus(outcome.status())
                .setLatencyMs(outcome.latencyMs())
                .setErrorMsg(outcome.errorMsg())
                .setCreatedAt(OffsetDateTime.now());

        results.save(res);
        log.info("Checked {} -> {} ({} ms{})",
                t.getUrl(),
                outcome.status(),
                outcome.latencyMs(),
                outcome.errorMsg() != null ? ", err=" + outcome.errorMsg() : "");
    }
}
