package com.garethahealy.wssecurity.https.cxf.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garethahealy.helloworld.HelloWorldEndpoint;

public class EndpointConfig {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointService.class);
	
	public HelloWorldEndpoint getEndpoint(boolean isCxfDebug) {
		List<Interceptor<? extends Message>> inInterceptors = new ArrayList<Interceptor<? extends Message>>();
		List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (isCxfDebug) {
			LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
			loggingInInterceptor.setPrettyLogging(true);
			
			LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
			loggingOutInterceptor.setPrettyLogging(true);
			
			inInterceptors.add(loggingInInterceptor);
			outInterceptors.add(loggingOutInterceptor);
		}
		
		outInterceptors.add(getWSS4JOutInterceptor());
		
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.getInInterceptors().addAll(inInterceptors);
		factoryBean.getOutInterceptors().addAll(outInterceptors);
		factoryBean.setServiceClass(HelloWorldEndpoint.class);
		factoryBean.setAddress("https://0.0.0.0:9001/cxf/helloWorldService");
		
		HelloWorldEndpoint port = (HelloWorldEndpoint) factoryBean.create();
		
		configureSSLOnTheClient(port);
		
		return port;
	}
	
	private static void configureSSLOnTheClient(Object c) {
        org.apache.cxf.endpoint.Client client = ClientProxy.getClient(c);
        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
 
        try {
            TLSClientParameters tlsParams = new TLSClientParameters();
            tlsParams.setDisableCNCheck(true);
 
            KeyStore keyStore = KeyStore.getInstance("JKS");
            String trustpass = "password";
 
            File truststore = new File("/NotBackedUp/jboss-studio-workspace/jboss-fuse-examples/ws-security-https-cxf/src/main/resources/certs/truststore.jks");
            keyStore.load(new FileInputStream(truststore), trustpass.toCharArray());
           
            TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(keyStore);
            TrustManager[] tm = trustFactory.getTrustManagers();
            tlsParams.setTrustManagers(tm);
 
            File truststoreWibble = new File("/NotBackedUp/jboss-studio-workspace/jboss-fuse-examples/ws-security-https-cxf-client/src/main/resources/certs/wibble.jks");
            keyStore.load(new FileInputStream(truststoreWibble), trustpass.toCharArray());
           
            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(keyStore, trustpass.toCharArray());
            KeyManager[] km = keyFactory.getKeyManagers();
            tlsParams.setKeyManagers(km);
 
            FiltersType filter = new FiltersType();
            filter.getInclude().add(".*_WITH_3DES_.*");
            filter.getInclude().add(".*_WITH_DES_.*");
            filter.getInclude().add(".*_WITH_NULL_.*");
            filter.getExclude().add(".*_DH_anon_.*");
            tlsParams.setCipherSuitesFilter(filter);

            httpConduit.setTlsClientParameters(tlsParams);
        } catch (KeyStoreException kse) {
        	LOG.error("Security configuration failed with the following: " + kse.getCause());
        } catch (NoSuchAlgorithmException nsa) {
        	LOG.error("Security configuration failed with the following: " + nsa.getCause());
        } catch (FileNotFoundException fnfe) {
        	LOG.error("Security configuration failed with the following: " + fnfe.getCause());
        } catch (UnrecoverableKeyException uke) {
        	LOG.error("Security configuration failed with the following: " + uke.getCause());
        } catch (CertificateException ce) {
        	LOG.error("Security configuration failed with the following: " + ce.getCause());
        } catch (IOException ioe) {
        	LOG.error("Security configuration failed with the following: " + ioe.getCause());
        }
    }
	
	private WSS4JOutInterceptor getWSS4JOutInterceptor() {
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "UsernameToken Timestamp");
        outProps.put("passwordType", "PasswordDigest"); //PasswordText
        outProps.put("user", "user.gareth");
        outProps.put("passwordCallbackClass", "com.garethahealy.wssecurity.https.cxf.client.impl.UTPasswordCallback");

        WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(outProps);
        return wss4j;
	}
}
