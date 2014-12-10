package com.garethahealy.wssecurity.https.cxf.client.config;

import java.util.Map;
import java.util.Properties;

public class WSCryptoProperties extends Properties {

    private static final long serialVersionUID = 7247785784943933835L;

    public WSCryptoProperties(Map<String, String> props) {
        this.putAll(props);
    }
}
