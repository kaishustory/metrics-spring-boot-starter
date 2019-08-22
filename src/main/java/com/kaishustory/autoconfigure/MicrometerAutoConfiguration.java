package com.kaishustory.autoconfigure;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
}
