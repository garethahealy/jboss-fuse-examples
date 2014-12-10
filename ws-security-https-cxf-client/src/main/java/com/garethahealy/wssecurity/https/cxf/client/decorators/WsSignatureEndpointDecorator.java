package com.garethahealy.wssecurity.https.cxf.client.decorators;

import com.garethahealy.wssecurity.https.cxf.client.config.WSCryptoProperties;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

import java.util.HashMap;
import java.util.Map;

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
