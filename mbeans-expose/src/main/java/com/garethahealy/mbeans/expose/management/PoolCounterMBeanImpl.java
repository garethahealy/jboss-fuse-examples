/*
 * #%L
 * mbeans-expose
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
package com.garethahealy.mbeans.expose.management;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.garethahealy.mbeans.expose.PoolCounter;

import org.apache.camel.CamelContext;

public class PoolCounterMBeanImpl extends BaseMBean<PoolCounterMBean> implements PoolCounterMBean {

    private String contextName;
    private PoolCounter poolCounter;
    private ObjectName objectName;

    public PoolCounterMBeanImpl(CamelContext context, MBeanServer mBeanServer, PoolCounter poolCounter) throws NotCompliantMBeanException {
        super(mBeanServer, PoolCounterMBean.class);

        this.contextName = context.getName();
        this.poolCounter = poolCounter;
    }

    @Override
    public void registerMBean() {
        if (objectName == null) {
            objectName = getObjectNameForBean(contextName, getClass().getSimpleName());
        }

        registerMBean(this, objectName);
    }

    @Override
    public void unregisterMBean() {
        if (objectName == null) {
            objectName = getObjectNameForBean(contextName, getClass().getSimpleName());
        }

        unregisterMBean(objectName);
    }

    @Override
    public int poolCount() {
        return poolCounter.poolCount();
    }
}
