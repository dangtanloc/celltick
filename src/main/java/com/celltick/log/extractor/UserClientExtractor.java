package com.celltick.log.extractor;

import com.celltick.log.common.MetricIndex;

import ua_parser.Client;
import ua_parser.Parser;

public abstract class UserClientExtractor implements MetricExtractor {

    public abstract String extract(String[] valueArr);

    protected Client extractUserClient(String[] valueArr) {
        Parser uaParser = new Parser();
        return uaParser.parse(valueArr[MetricIndex.USER_AGENT.getIndex()]);
    }
}
