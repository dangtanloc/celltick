package com.celltick.log.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.celltick.log.common.CommonConstant;
import com.celltick.log.controller.request.MetricLogRequest;
import com.celltick.log.controller.response.MetricLogResponse;
import com.celltick.log.model.DistributionCalculation;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetricService {

    private final DistributionCalculation countryCalculator;
    private final DistributionCalculation osCalculator;
    private final DistributionCalculation browserCalculator;

    private Map<String, DistributionCalculation> calculationMap;

    public MetricService(@Qualifier("Country_Metric") DistributionCalculation countryCalculator,
                         @Qualifier("Browser_Metric") DistributionCalculation browserCalculator,
                         @Qualifier("OS_Metric") DistributionCalculation osCalculator) {
        this.countryCalculator = countryCalculator;
        this.osCalculator = osCalculator;
        this.browserCalculator = browserCalculator;

        calculationMap = new HashMap<>(3);
        calculationMap.put("Countries", countryCalculator);
        calculationMap.put("Browsers", browserCalculator);
        calculationMap.put("Operating systems", osCalculator);

    }

    public MetricLogResponse processMetricLogFile(MetricLogRequest request) {

        List<String> successMetrics = request.getMetrics().stream().filter(
                CommonConstant.supportedMetric::contains).collect(Collectors.toList());

        List<String> failedMetrics = request.getMetrics().stream().filter(s ->
                                                                                  !CommonConstant.supportedMetric
                                                                                          .contains(s)).collect(
                Collectors.toList());

        List<DistributionCalculation> calculationList = successMetrics.stream().map(
                metric -> calculationMap.get(metric)).collect(Collectors.toList());
        Long startTime = System.currentTimeMillis();
        processFile(request.getFilename(), calculationList);
        Long endTime = System.currentTimeMillis();

        log.info("Process Time : " + (endTime - startTime) / 1000 + " seconds");

        return MetricLogResponse.builder().filename(request.getFilename()).successMetrics(successMetrics)
                                .failedMetrics(failedMetrics).build();

    }

    private void processFile(String fileName, List<DistributionCalculation> calculationList) {

        int row = 0;
        CSVParser csvParser = new CSVParserBuilder().withSeparator(' ').withQuoteChar('"').build();

        try (final CSVReader csvReader = new CSVReaderBuilder(
                new FileReader(fileName))
                .withCSVParser(csvParser)   // custom CSV parser
                .build()) {

            // Process by bach size 100, to increase process time
            List<String[]> batchValue = new ArrayList<>(10);
            for (String[] valueArr; (valueArr = csvReader.readNext()) != null; row++) {
                batchValue.add(valueArr);
                if (row >= 10) {

                    calculationList.forEach(cal -> {
                        cal.count(batchValue);
                    });

                    row = 0;
                    batchValue.clear();
                }

            }

            if (!batchValue.isEmpty()) {
                calculationList.forEach(cal -> {
                    cal.count(batchValue);
                });
            }
        } catch (final Exception e) {
            log.error("Can not parse file : {}", fileName);
        }

        calculationList.forEach(cal -> {
            log.info("Metric type : " + cal.getName());
            log.info("Total row : " + cal.getTotal());
            cal.printResult();
        });

    }

}
