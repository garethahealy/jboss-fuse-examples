package com.garethahealy.wssecurity.https.cxf.impl;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HTTPMethodInterceptor extends AbstractSoapInterceptor {

    private static final Map<String, Boolean> httpMethods;

    static {
        Map<String, Boolean> httpMap = new HashMap<String, Boolean>();
        httpMap.put("PUT", false);
        httpMap.put("GET", true);

        httpMethods = Collections.unmodifiableMap(httpMap);
    }

    public HTTPMethodInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
        if (request != null) {
            if (!httpMethods.get(request.getMethod())) {
                throw new Fault(new IllegalArgumentException("Invalid HTTPMethod of " + request.getMethod()));
            }
        }
    }
}
