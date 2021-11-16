package com.celltick.log.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.celltick.log.extractor.BrowserExtractor;
import com.celltick.log.extractor.CountryExtractor;
import com.celltick.log.extractor.OSExtractor;
import com.celltick.log.model.DistributionCalculation;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DistributionCalculationConfig {

    private final CountryExtractor countryExtractor;

    private final BrowserExtractor browserExtractor;

    private final OSExtractor osExtractor;

    @Bean(name = "Country_Metric")
    public DistributionCalculation countryDistributionCalculation() {
        return new DistributionCalculation(countryExtractor,"Countries");
    }

    @Bean(name = "Browser_Metric")
    public DistributionCalculation browserDistributionCalculation() {
        return new DistributionCalculation(browserExtractor, "Browsers");
    }
    @Bean(name = "OS_Metric")
    public DistributionCalculation osDistributionCalculation() {
        return new DistributionCalculation(osExtractor,"Operating systems");
    }

}
