package com.garethahealy.wssecurity.https.cxf.impl;

import java.util.Map;
import java.util.Properties;

public class WSCryptoProperties extends Properties {

	private static final long serialVersionUID = 1444412430262829248L;

	public WSCryptoProperties(Map<String, String> props) {
		this.putAll(props);
	}
}
