package com.celltick.log.common;

import lombok.Getter;

public enum MetricIndex {
    IP_ADDRESS(0), USER_AGENT(9);
    @Getter
    private int index;

    MetricIndex(int index) {
        this.index = index;
    }
}
