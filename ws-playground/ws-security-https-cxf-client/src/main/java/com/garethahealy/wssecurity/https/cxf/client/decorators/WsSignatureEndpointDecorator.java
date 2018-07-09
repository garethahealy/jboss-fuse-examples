/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF Client
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
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

import java.util.HashMap;
import java.util.Map;

import com.garethahealy.wssecurity.https.cxf.client.config.WSCryptoProperties;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;

import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

public class WsSignatureEndpointDecorator extends LoggingWsEndpointDecorator {

    public WsSignatureEndpointDecorator(WsEndpointConfiguration<?> config) {
        super(config);
    }

    @Override
    public synchronized Object create() {
        this.getOutInterceptors().add(getWSS4JOutInterceptor());

        return super.create();
    }

    public WSS4JOutInterceptor getWSS4JOutInterceptor() {
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "Timestamp Signature");
        outProps.put("signaturePropRefId", "wsCryptoProperties");
        outProps.put("wsCryptoProperties", getWSCryptoProperties());
        outProps.put("signatureUser", config.getCertifactionAlias());
        outProps.put("passwordType", "PasswordText");
        outProps.put("passwordCallbackClass", config.getPasswordCallbackClass());

        WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(outProps);
        return wss4j;
    }

    private WSCryptoProperties getWSCryptoProperties() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        map.put("org.apache.ws.security.crypto.merlin.keystore.type", "jks");
        map.put("org.apache.ws.security.crypto.merlin.keystore.password", config.getSignatureKeystorePassword());
        map.put("org.apache.ws.security.crypto.merlin.keystore.file", config.getSignatureKeystoreFilename());

        return new WSCryptoProperties(map);
    }
}
