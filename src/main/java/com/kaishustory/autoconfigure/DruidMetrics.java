package com.kaishustory.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class DruidMetrics implements MeterBinder {
    private DruidDataSource druidDataSource;

    public DruidMetrics(DruidDataSource druidDataSource) {
        this.druidDataSource = druidDataSource;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        // todo 增加tag，线程池名
        Gauge.builder("test.druid.pooling.count", druidDataSource, DruidDataSource::getPoolingCount)
                .register(registry);

        Gauge.builder("test.druid.close.count", druidDataSource, DruidDataSource::getCloseCount)
                .register(registry);

        Gauge.builder("test.druid.commit.count", druidDataSource, DruidDataSource::getCommitCount)
                .register(registry);

        Gauge.builder("test.druid.error.count", druidDataSource, DruidDataSource::getErrorCount)
                .register(registry);
    }
}