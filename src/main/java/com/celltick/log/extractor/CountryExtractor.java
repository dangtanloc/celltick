package com.celltick.log.extractor;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.celltick.log.common.MetricIndex;
import com.celltick.log.service.GeoIpService;
import com.maxmind.geoip2.model.CityResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountryExtractor implements MetricExtractor {

    private final GeoIpService geoIpService;

    @Override
    public String extract(String[] valueArr) {
        String country = "Unknown";
        try {
            CityResponse cityResponse = geoIpService.getLocation(valueArr[MetricIndex.IP_ADDRESS.getIndex()]);
             if(Objects.nonNull(cityResponse.getCountry().getName())){
                 return cityResponse.getCountry().getName();
             }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return country;
    }
}
