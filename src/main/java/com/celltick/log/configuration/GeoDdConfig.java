package com.celltick.log.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.celltick.log.common.GeoDdDatasource;

@Configuration
public class GeoDdConfig {
    @Value("${geo-ip.localtion}")
    private String geoDbFile;

    @Bean
    public GeoDdDatasource geoDdDatasource() throws IOException {
        return GeoDdDatasource.getInstance(geoDbFile);
    }
}
