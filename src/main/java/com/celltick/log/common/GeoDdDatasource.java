package com.celltick.log.common;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import com.maxmind.geoip2.DatabaseReader;

import lombok.Getter;

public class GeoDdDatasource {

    private static GeoDdDatasource geoDdDatasource;

    @Getter
    private DatabaseReader dbReader;

    private GeoDdDatasource(String geoDbFile) throws IOException {
        File database = new ClassPathResource(geoDbFile).getFile();
        dbReader = new DatabaseReader.Builder(database).build();
    }

    static public GeoDdDatasource getInstance(String geoDbFile) throws IOException {
        if (geoDdDatasource == null) {
            synchronized (GeoDdDatasource.class) {
                if (geoDdDatasource == null) {
                    geoDdDatasource = new GeoDdDatasource(geoDbFile);
                }
            }
        }

        return geoDdDatasource;
    }
}
