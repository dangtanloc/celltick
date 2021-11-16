package com.celltick.log.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.celltick.log.common.PercentageCalculator;
import com.celltick.log.extractor.MetricExtractor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DistributionCalculation {
    private String name;
    private Long total;
    private Map<String, Integer> counter;
    private MetricExtractor metricExtractor;
    private ExecutorService executor;

    public DistributionCalculation(MetricExtractor metricExtractor, String name) {
        this.counter = new HashMap<>(100);
        total = 0L;
        this.name = name;
        this.metricExtractor = metricExtractor;
        executor = Executors.newFixedThreadPool(5);
    }

    public void count(List<String[]> rawValueList) {


        List<Callable<String>> callables =   new ArrayList<>(rawValueList.size());

        rawValueList.forEach(rawValue->{
            Callable<String> test = () ->  metricExtractor.extract(rawValue);
            callables.add(test);
        });


        try {
            executor.invokeAll(callables).forEach(futureMetricValue -> {
                String metricValue = null;
                try {
                    metricValue = futureMetricValue.get();

                    if (counter.containsKey(metricValue)) {
                        int count = counter.get(metricValue) + 1;
                        counter.put(metricValue, count);
                    } else {
                        counter.put(metricValue, 1);
                    }
                    total++;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Map<String, String> calculate() {
        Map<String, String> distribution = new HashMap<>(counter.size());
        counter.forEach((key, value) -> {
            distribution.put(key,
                             String.format("%.2f", PercentageCalculator.calculatePercentage(value, total)));
        });

        return distribution;
    }

    public void printResult() {
        Map<String, String> result = calculate();

        result.forEach((key, value) -> {
            log.info("{} : {}%", key, value);
        });

    }

}
