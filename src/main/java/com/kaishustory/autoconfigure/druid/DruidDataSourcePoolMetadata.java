package com.kaishustory.autoconfigure.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;

public class DruidDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<DruidDataSource> {
    public DruidDataSourcePoolMetadata(DruidDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Integer getActive() {
        return getDataSource().getActiveCount();
    }

    @Override
    public Integer getMax() {
        return getDataSource().getMaxActive();
    }

    @Override
    public Integer getMin() {
        return getDataSource().getMinIdle();
    }

    @Override
    public String getValidationQuery() {
        return getDataSource().getValidationQuery();
    }

    @Override
    public Boolean getDefaultAutoCommit() {
        return getDataSource().isDefaultAutoCommit();
    }
}
