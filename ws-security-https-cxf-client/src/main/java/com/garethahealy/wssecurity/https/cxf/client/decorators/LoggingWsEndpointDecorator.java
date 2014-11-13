package com.garethahealy.wssecurity.https.cxf.client.decorators;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;

import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;

public class LoggingWsEndpointDecorator extends JaxWsProxyFactoryBean {

	protected WsEndpointConfiguration<?> config;
	
	public LoggingWsEndpointDecorator(WsEndpointConfiguration<?> config) {
		this.config = config;
	}
	
	@Override
    public synchronized Object create() {
		this.getInInterceptors().addAll(getLogInInterceptors());
		this.getOutInterceptors().addAll(getLogOutInterceptors());
		this.setServiceClass(config.getServiceClass());
		this.setAddress(config.getWsAddress());

		return super.create();
	}
	
	public List<Interceptor<? extends Message>> getLogInInterceptors() {
		List<Interceptor<? extends Message>> inInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (config.isCxfDebug()) {
			LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
			loggingInInterceptor.setPrettyLogging(true);
			
			inInterceptors.add(loggingInInterceptor);
		}
		
		return inInterceptors;
	}
	
	public List<Interceptor<? extends Message>> getLogOutInterceptors() {
		List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (config.isCxfDebug()) {	
			LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
			loggingOutInterceptor.setPrettyLogging(true);
			
			outInterceptors.add(loggingOutInterceptor);
		}

		return outInterceptors;
	}
}
