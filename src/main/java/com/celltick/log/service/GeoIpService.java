package com.celltick.log.service;

import java.io.IOException;
import java.net.InetAddress;

import org.springframework.stereotype.Service;

import com.celltick.log.common.GeoDdDatasource;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoIpService {

    private final GeoDdDatasource geoDdDatasource;

    public CityResponse getLocation(String ipAddress) throws IOException, GeoIp2Exception {
        return geoDdDatasource.getDbReader().city(
                InetAddress.getByName(ipAddress));
    }

}
