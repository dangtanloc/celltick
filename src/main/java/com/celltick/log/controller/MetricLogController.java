package com.celltick.log.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.celltick.log.controller.request.MetricLogRequest;
import com.celltick.log.controller.response.MetricLogResponse;
import com.celltick.log.service.MetricService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/metric")
@RequiredArgsConstructor
public class MetricLogController {

    private final MetricService metricService;

    @PostMapping
    public MetricLogResponse process(@RequestBody MetricLogRequest metricLogRequest) {
        return metricService.processMetricLogFile(metricLogRequest);
    }
}
