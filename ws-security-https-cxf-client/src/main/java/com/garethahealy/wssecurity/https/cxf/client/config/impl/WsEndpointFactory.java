package com.garethahealy.wssecurity.https.cxf.client.config.impl;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.garethahealy.wssecurity.https.cxf.client.config.WsTLSClientDecorator;

public class WsEndpointFactory {

	private WsInterceptorFactory wsInterceptorFactory;
	private WsTLSClientDecorator tlsClientdecorator;
	private WsEndpointConfiguration<?> config;
	
	public WsEndpointFactory(WsInterceptorFactory wsInterceptorFactory, WsTLSClientDecorator tlsClientdecorator,
			WsEndpointConfiguration<?> config) {
		this.wsInterceptorFactory = wsInterceptorFactory;
		this.tlsClientdecorator = tlsClientdecorator;
		this.config = config;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getEndpoint() {
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.getInInterceptors().addAll(wsInterceptorFactory.getInInterceptors());
		factoryBean.getOutInterceptors().addAll(wsInterceptorFactory.getOutInterceptors());
		factoryBean.setServiceClass(config.getServiceClass());
		factoryBean.setAddress(config.getWsAddress());
		
		T port = (T)factoryBean.create();

		tlsClientdecorator.configureSSLOnTheClient(ClientProxy.getClient(port));

		return port;
	}
}
