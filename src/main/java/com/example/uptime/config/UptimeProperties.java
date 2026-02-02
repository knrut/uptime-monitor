package com.example.uptime.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import reactor.core.scheduler.Scheduler;

@Getter @Setter
@ConfigurationProperties(prefix = "uptime")
public class UptimeProperties {

    public Scheduler scheduler = new Scheduler();
    public Http http = new Http();

    @Getter @Setter
    public static class Scheduler {
        public int tickSec = 5;
        public int parallelism = 4;
    }

    @Getter @Setter
    public static class Http {
        public int connectTimeoutMs = 2000;
        public int readTimeoutMs = 5000;
        public boolean followRedirects = true;
        public String userAgent = "UptimeMonitor/1.0";
    }
}
