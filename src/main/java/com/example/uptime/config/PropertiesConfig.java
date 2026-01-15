package com.example.uptime.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UptimeProperties.class)
public class PropertiesConfig {}
