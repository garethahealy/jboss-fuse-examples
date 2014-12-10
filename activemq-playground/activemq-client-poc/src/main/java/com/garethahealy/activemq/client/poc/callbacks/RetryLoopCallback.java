/*
 * #%L
 * activemq-client-poc
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
package com.garethahealy.activemq.client.poc.callbacks;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RetryLoopCallback {

    public <T> T runAndGetResult() throws JMSException {
        //NOOP
        return null;
    }

    public void run() throws JMSException {
        //NOOP
    }

    public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
        //NOOP
    }

    protected Object[] getLoggingArguments(JMSException ex, int count, int retryAmount, Object... arguments) {
        List<Object> items = new ArrayList<Object>(Arrays.asList(arguments));
        items.add(ex.getMessage());
        items.add(count);
        items.add(retryAmount);

        return items.toArray(new Object[items.size()]);
    }
}
