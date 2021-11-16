package com.celltick.log.extractor;

import org.springframework.stereotype.Component;

import ua_parser.Client;

@Component
public class BrowserExtractor extends UserClientExtractor {
    @Override
    public String extract(String[] valueArr) {
        Client client = this.extractUserClient(valueArr);
        return client.userAgent.family;
    }
}
