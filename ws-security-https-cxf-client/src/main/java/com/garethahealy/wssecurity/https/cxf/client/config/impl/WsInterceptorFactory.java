package com.garethahealy.wssecurity.https.cxf.client.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

public class WsInterceptorFactory {

	public List<Interceptor<? extends Message>> getInInterceptors(WsEndpointConfiguration<?> config) {
		List<Interceptor<? extends Message>> inInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (config.isCxfDebug()) {
			LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
			loggingInInterceptor.setPrettyLogging(true);
			
			inInterceptors.add(loggingInInterceptor);
		}
		
		return inInterceptors;
	}
	
	public List<Interceptor<? extends Message>> getOutInterceptors(WsEndpointConfiguration<?> config) {
		List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (config.isCxfDebug()) {	
			LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
			loggingOutInterceptor.setPrettyLogging(true);
			
			outInterceptors.add(loggingOutInterceptor);
		}
		
		outInterceptors.add(getWSS4JOutInterceptor(config));
		
		return outInterceptors;
	}
	
	private WSS4JOutInterceptor getWSS4JOutInterceptor(WsEndpointConfiguration<?> config) {
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "Timestamp Signature");
        outProps.put("signaturePropFile", config.getSignaturePropFile());
        outProps.put("signatureUser", config.getCertifactionAlias());
        outProps.put("passwordType", "PasswordText");
        outProps.put("passwordCallbackClass", config.getPasswordCallbackClass());
   
        WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(outProps);
        return wss4j;
	}
}
