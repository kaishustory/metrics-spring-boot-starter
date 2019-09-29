package com.kaishustory.autoconfigure.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({MetricsAutoConfiguration.class, DataSourceAutoConfiguration.class,
        SimpleMetricsExportAutoConfiguration.class})
@ConditionalOnClass(DruidDataSource.class)
public class DruidMetricsAutoConfiguration {
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public DruidMetrics druidMetrics(DataSource dataSource) {
        return new DruidMetrics(DataSourceUnwrapper.unwrap(dataSource,
                DruidDataSource.class));
    }

    @Bean
    public DataSourcePoolMetadataProvider druidPoolDataSourceMetadataProvider() {
        return dataSource -> {
            DruidDataSource druidDataSource = DataSourceUnwrapper.unwrap(dataSource,
                    DruidDataSource.class);
            if (druidDataSource != null) {
                return new DruidDataSourcePoolMetadata(druidDataSource);
            }
            return null;
        };
    }
}
