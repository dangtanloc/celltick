package com.celltick.log.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistributionResult {
    private String key;
    private Double distribution;
}
