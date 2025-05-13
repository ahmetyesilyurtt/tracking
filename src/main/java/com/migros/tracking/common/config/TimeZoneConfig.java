package com.migros.tracking.common.config;

import com.migros.tracking.common.util.DateUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@ConditionalOnProperty(value = "timezone.config.enabled", havingValue = "true", matchIfMissing = true)
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(DateUtils.ZONE_UTC));
    }
}
