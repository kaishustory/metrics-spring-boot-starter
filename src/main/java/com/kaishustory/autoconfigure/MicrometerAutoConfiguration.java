package com.kaishustory.autoconfigure;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@Configuration
@AutoConfigureAfter(MetricsAutoConfiguration.class)
@Import({TimedAspectConfiguration.class, CountedAspectConfiguration.class})
public class MicrometerAutoConfiguration {
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name:UNKNOWN}") String applicationName,
                                                             @Value("${spring.profiles.active:UNKNOWN}") String profilesActive) {
        return registry -> registry.config().commonTags("application", applicationName, "profile", profilesActive);
    }

    @Bean
    @Order(0)
    public MeterFilter meterFilter() {
        return MeterFilter.deny(id -> {
            String uri = id.getTag("uri");
            if (StringUtils.isNotBlank(uri)) {
                return StringUtils.startsWith(uri, "/actuator") || StringUtils.contains(uri, "favicon.ico") || !StringUtils.startsWith(uri, "/");
            }

            return false;
        });
    }
}
