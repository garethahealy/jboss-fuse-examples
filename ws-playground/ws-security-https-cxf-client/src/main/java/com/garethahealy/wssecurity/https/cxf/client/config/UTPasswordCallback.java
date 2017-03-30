/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF Client
 * %%
 * Copyright (C) 2013 - 2017 Gareth Healy
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
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.garethahealy.wssecurity.https.cxf.client.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback handler to handle passwords
 */
public class UTPasswordCallback implements CallbackHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UTPasswordCallback.class);

    private Map<String, String> passwords = new HashMap<String, String>();

    public UTPasswordCallback() {
        passwords.put("user.gareth", "healy");
        passwords.put("clientx509v1", "storepassword");
    }

    /**
     * Here, we attempt to get the password from the private alias/passwords map.
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        String user = "";

        for (Callback callback : callbacks) {
            String pass = passwords.get(getIdentifier(callback));
            if (pass != null) {
                setPassword(callback, pass);
                return;
            }

        }

        // Password not found
        throw new IOException("Password does not exist for the user : " + user);
    }

    private void setPassword(Callback callback, String pass) {
        try {
            callback.getClass().getMethod("setPassword", String.class).invoke(callback, pass);
        } catch (NoSuchMethodException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (SecurityException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (IllegalAccessException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (IllegalArgumentException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (InvocationTargetException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    private String getIdentifier(Callback cb) {
        try {
            return (String)cb.getClass().getMethod("getIdentifier").invoke(cb);
        } catch (Exception ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }

        return null;
    }

    /**
     * Add an alias/password pair to the callback mechanism.
     */
    public void setAliasPassword(String alias, String password) {
        passwords.put(alias, password);
    }
}
