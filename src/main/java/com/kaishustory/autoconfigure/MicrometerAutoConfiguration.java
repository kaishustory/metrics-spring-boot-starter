package com.kaishustory.autoconfigure;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(CompositeMeterRegistryAutoConfiguration.class)
public class MicrometerAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "spring.application", name = "name")
    public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName,
                                                             @Value("${spring.profiles.active}") String profilesActive) {
        return registry -> registry.config().commonTags("application", applicationName, "profile", profilesActive);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Timed.class)
    @ConditionalOnBean(MeterRegistry.class)
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Counted.class)
    @ConditionalOnBean(MeterRegistry.class)
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
