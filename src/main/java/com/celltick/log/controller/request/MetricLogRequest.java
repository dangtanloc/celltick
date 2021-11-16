package com.celltick.log.controller.request;

import java.util.List;

import lombok.Data;

@Data
public class MetricLogRequest {
    private String filename;
    private List<String> metrics;
}
