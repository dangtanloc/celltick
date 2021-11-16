package com.celltick.log.model;

import java.util.ArrayList;
import java.util.Comparator;
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

        List<Callable<String>> callables = new ArrayList<>(rawValueList.size());

        rawValueList.forEach(rawValue -> {
            Callable<String> test = () -> metricExtractor.extract(rawValue);
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

    private List<DistributionResult> calculate() {
        List<DistributionResult> listResult = new ArrayList<>(counter.size());
        counter.forEach((key, value) -> {
            listResult.add(DistributionResult.builder().key(key).distribution(
                    PercentageCalculator.calculatePercentage(value, total)).build());
        });

        return listResult;
    }

    public void printResult() {
        List<DistributionResult> result = calculate();

        result.stream().sorted(Comparator.comparing(DistributionResult::getDistribution).reversed()).forEach(
                (item) -> {
                    log.info("{} : {}%", item.getKey(), String.format("%.2f",item.getDistribution()));
                });
    }

}
