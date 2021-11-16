package com.celltick.log.controller.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricLogResponse {

    private String filename;
    private List<String> successMetrics;
    private List<String> failedMetrics;
}
