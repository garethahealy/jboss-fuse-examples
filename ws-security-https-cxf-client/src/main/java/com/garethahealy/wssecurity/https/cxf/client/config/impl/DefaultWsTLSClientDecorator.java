package com.garethahealy.wssecurity.https.cxf.client.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garethahealy.wssecurity.https.cxf.client.config.WsTLSClientDecorator;

public class DefaultWsTLSClientDecorator implements WsTLSClientDecorator {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultWsTLSClientDecorator.class);

	public void configureSSLOnTheClient(WsEndpointConfiguration<?> config, Client client) {
		HTTPConduit httpConduit = (HTTPConduit) client.getConduit();

		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			
			//NOTE: The below order matters!
			File keyStoreClient = new File(config.getKeystorePath());
			keyStore.load(new FileInputStream(keyStoreClient), config.getKeystorePassword().toCharArray());
			
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyFactory.init(keyStore, config.getKeyManagerPassword().toCharArray());
			
			File truststoreClient = new File(config.getTruststorePath());
			keyStore.load(new FileInputStream(truststoreClient), config.getTruststorePassword().toCharArray());
			
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(keyStore);
			
			FiltersType filter = new FiltersType();
			filter.getInclude().add(".*_WITH_3DES_.*");
			filter.getInclude().add(".*_WITH_DES_.*");
			filter.getInclude().add(".*_WITH_NULL_.*");
			filter.getExclude().add(".*_DH_anon_.*");
			
			TLSClientParameters tlsParams = new TLSClientParameters();
			tlsParams.setDisableCNCheck(true);
			tlsParams.setTrustManagers(trustFactory.getTrustManagers());
			tlsParams.setKeyManagers(keyFactory.getKeyManagers());
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
}
