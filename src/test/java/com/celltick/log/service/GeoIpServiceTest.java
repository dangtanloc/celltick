package com.celltick.log.service;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxmind.geoip2.exception.GeoIp2Exception;

@SpringBootTest
public class GeoIpServiceTest {

    @Autowired
    private GeoIpService geoIpService;

    @Test
    public void testDetectIP() throws IOException, GeoIp2Exception {
        String ipAddress = "";
        System.out.println(geoIpService.getLocation("65.34.248.51"));
    }
}
