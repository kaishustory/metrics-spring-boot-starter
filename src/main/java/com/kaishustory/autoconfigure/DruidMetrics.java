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
        Gauge.builder("druid.pooling.count", druidDataSource, DruidDataSource::getPoolingCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.pooling.peak", druidDataSource, DruidDataSource::getPoolingPeak)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.close.count", druidDataSource, DruidDataSource::getCloseCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.commit.count", druidDataSource, DruidDataSource::getCommitCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.error.count", druidDataSource, DruidDataSource::getErrorCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.active.count", druidDataSource, DruidDataSource::getActiveCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.active.peak", druidDataSource, DruidDataSource::getActivePeak)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.create.count", druidDataSource, DruidDataSource::getCreateCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.connect.count", druidDataSource, DruidDataSource::getConnectCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.destroy.count", druidDataSource, DruidDataSource::getDestroyCount)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.max.active", druidDataSource, DruidDataSource::getMaxActive)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.max.idle", druidDataSource, DruidDataSource::getMaxIdle)
                .tag("pool", druidDataSource.getName())
                .register(registry);

        Gauge.builder("druid.min.idle", druidDataSource, DruidDataSource::getMinIdle)
                .tag("pool", druidDataSource.getName())
                .register(registry);
    }
}