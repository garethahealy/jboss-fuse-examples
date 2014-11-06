package com.garethahealy.wssecurity.https.cxf.client.config;

import org.apache.cxf.endpoint.Client;

public interface WsTLSClientDecorator {

	void configureSSLOnTheClient(Client client);
}
