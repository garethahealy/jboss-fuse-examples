/*
 * #%L
 * ws-security-https-cxf-client
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.wssecurity.https.cxf.client.decorators;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPSWsSignatureEndpointDecorator extends WsSignatureEndpointDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(HTTPSWsSignatureEndpointDecorator.class);

    public HTTPSWsSignatureEndpointDecorator(WsEndpointConfiguration<?> config) {
        super(config);
    }

    @Override
    public synchronized Object create() {
        Object port = super.create();

        Client client = ClientProxy.getClient(port);
        configureSSLOnTheClient(client);

        return port;
    }

    public void configureSSLOnTheClient(Client client) {
        //NOTE: The below order matters!
        HTTPConduit httpConduit = (HTTPConduit)client.getConduit();

        KeyStore keyStore = getInstanceOfKeyStore();

        loadKeyStore(keyStore, config.getKeystorePath(), config.getKeystorePassword());

        KeyManagerFactory keyFactory = getInstanceOfKeyManagerFactory(keyStore, config.getKeyManagerPassword());

        loadKeyStore(keyStore, config.getTruststorePath(), config.getTruststorePassword());

        TrustManagerFactory trustFactory = getInstanceOfTrustManagerFactory(keyStore);

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
    }

    private KeyStore getInstanceOfKeyStore() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("JKS");
        } catch (KeyStoreException kse) {
            LOG.error("Security configuration failed with the following: " + kse.getCause());
        }

        return keyStore;
    }

    private void loadKeyStore(KeyStore keyStore, String path, String storePassword) {
        File pathFile = new File(path);
        try (FileInputStream stream = new FileInputStream(pathFile)) {
            keyStore.load(stream, storePassword.toCharArray());
        } catch (IOException nsa) {
            LOG.error("Security configuration failed with the following: " + nsa.getCause());
        } catch (CertificateException fnfe) {
            LOG.error("Security configuration failed with the following: " + fnfe.getCause());
        } catch (NoSuchAlgorithmException fnfe) {
            LOG.error("Security configuration failed with the following: " + fnfe.getCause());
        }
    }

    private KeyManagerFactory getInstanceOfKeyManagerFactory(KeyStore keyStore, String keyManagerPassword) {
        KeyManagerFactory keyFactory = null;
        try {
            keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(keyStore, keyManagerPassword.toCharArray());
        } catch (NoSuchAlgorithmException fnfe) {
            LOG.error("Security configuration failed with the following: " + fnfe.getCause());
        } catch (KeyStoreException kse) {
            LOG.error("Security configuration failed with the following: " + kse.getCause());
        } catch (UnrecoverableKeyException uke) {
            LOG.error("Security configuration failed with the following: " + uke.getCause());
        }

        return keyFactory;
    }

    private TrustManagerFactory getInstanceOfTrustManagerFactory(KeyStore keyStore) {
        TrustManagerFactory trustFactory = null;
        try {
            trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(keyStore);
        } catch (NoSuchAlgorithmException fnfe) {
            LOG.error("Security configuration failed with the following: " + fnfe.getCause());
        } catch (KeyStoreException kse) {
            LOG.error("Security configuration failed with the following: " + kse.getCause());
        }

        return trustFactory;
    }
}
