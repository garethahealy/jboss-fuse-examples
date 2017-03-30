/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF
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
package com.garethahealy.wssecurity.https.cxf.impl;

import java.util.Map;
import java.util.Properties;

public class WSCryptoProperties extends Properties {

    private static final long serialVersionUID = 1444412430262829248L;

    public WSCryptoProperties(Map<String, String> props) {
        this.putAll(props);
    }
}
