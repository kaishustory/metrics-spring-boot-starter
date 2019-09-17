package com.kaishustory.autoconfigure;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(CompositeMeterRegistryAutoConfiguration.class)
@Import({TimedAspectConfiguration.class, CountedAspectConfiguration.class})
public class MicrometerAutoConfiguration {
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name:UNKNOWN}") String applicationName,
                                                             @Value("${spring.profiles.active:UNKNOWN}") String profilesActive) {
        return registry -> registry.config().commonTags("application", applicationName, "profile", profilesActive);
    }

    @Bean
    @ConditionalOnMissingBean
    public MeterFilter meterFilter() {
        return MeterFilter.deny(id -> {
            String uri = id.getTag("uri");
            return StringUtils.startsWith(uri, "/actuator") || StringUtils.contains(uri, "favicon.ico") || !StringUtils.startsWith(uri, "/");
        });
    }
}
