package com.kaishustory.autoconfigure.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@AutoConfigureAfter({MetricsAutoConfiguration.class, DataSourceAutoConfiguration.class,
        SimpleMetricsExportAutoConfiguration.class})
@ConditionalOnClass(DruidDataSource.class)
public class DruidMetricsAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidMetricsAutoConfiguration.class);

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public DruidMetrics druidMetrics(List<DataSource> dataSourceList) {
        LOGGER.info("Found {} datasource", dataSourceList.size());
        Set<DruidDataSource> druidDataSourceList = new HashSet<>();

        for (DataSource dataSource : dataSourceList) {
            if (dataSource instanceof AbstractRoutingDataSource) {
                try {
                    if (dataSource.isWrapperFor(DruidDataSource.class)) {
                        druidDataSourceList.add(dataSource.unwrap(DruidDataSource.class));
                    }
                } catch (SQLException e) {
                    LOGGER.error("DataSource is not DruidDataSource", e);
                }
            } else if (dataSource instanceof DruidDataSource) {
                druidDataSourceList.add((DruidDataSource) dataSource);
            } else {
                LOGGER.info("None DruidDataSource found");
            }
        }

        for (DruidDataSource druidDataSource : druidDataSourceList) {
            LOGGER.info("Exporting metrics for {} datasource", druidDataSource.getName());
        }

        return new DruidMetrics(druidDataSourceList);
    }

    @Bean
    public DataSourcePoolMetadataProvider druidPoolDataSourceMetadataProvider() {
        return dataSource -> {
            if (dataSource instanceof AbstractRoutingDataSource) {
                try {
                    if (dataSource.isWrapperFor(DruidDataSource.class)) {
                        return new DruidDataSourcePoolMetadata(dataSource.unwrap(DruidDataSource.class));
                    }
                } catch (SQLException e) {
                    LOGGER.error("Initialize DataSourcePoolMetadataProvider error", e);
                }
            }

            if (dataSource instanceof DruidDataSource) {
                DruidDataSource druidDataSource = (DruidDataSource) dataSource;
                return new DruidDataSourcePoolMetadata(druidDataSource);
            }

            return null;
        };
    }
}
