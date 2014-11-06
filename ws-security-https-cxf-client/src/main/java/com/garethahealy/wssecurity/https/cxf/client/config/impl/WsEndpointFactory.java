package com.garethahealy.wssecurity.https.cxf.client.config.impl;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.garethahealy.wssecurity.https.cxf.client.config.WsTLSClientDecorator;

public class WsEndpointFactory {

	private WsInterceptorFactory wsInterceptorFactory;
	private WsTLSClientDecorator tlsClientdecorator;
	
	public WsEndpointFactory(WsInterceptorFactory wsInterceptorFactory, WsTLSClientDecorator tlsClientdecorator) {
		this.wsInterceptorFactory = wsInterceptorFactory;
		this.tlsClientdecorator = tlsClientdecorator;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getEndpoint(WsEndpointConfiguration<T> config) {
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.getInInterceptors().addAll(wsInterceptorFactory.getInInterceptors(config));
		factoryBean.getOutInterceptors().addAll(wsInterceptorFactory.getOutInterceptors(config));
		factoryBean.setServiceClass(config.getServiceClass());
		factoryBean.setAddress(config.getWsAddress());
		
		T port = (T)factoryBean.create();

		tlsClientdecorator.configureSSLOnTheClient(config, ClientProxy.getClient(port));
		
		return port;
	}
}
