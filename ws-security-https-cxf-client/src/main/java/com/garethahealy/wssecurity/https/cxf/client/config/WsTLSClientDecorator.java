package com.garethahealy.wssecurity.https.cxf.client.config;

import org.apache.cxf.endpoint.Client;

import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointConfiguration;

public interface WsTLSClientDecorator {

	void configureSSLOnTheClient(WsEndpointConfiguration<?> config, Client client);
}
