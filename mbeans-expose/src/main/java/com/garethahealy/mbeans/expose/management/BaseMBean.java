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

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseMBean<T> extends StandardMBean {

    private static final Logger LOG = LoggerFactory.getLogger(BaseMBean.class);

    private static final String DOMAIN_NAME = "com.garethahealy";
    private static final String KEY_BEAN = "beans";
    private static final String TYPE_BEAN = "beans";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";

    private MBeanServer mBeanServer;

    public BaseMBean(MBeanServer mBeanServer, Class<T> mbeanInterface) throws NotCompliantMBeanException {
        super(mbeanInterface);

        this.mBeanServer = mBeanServer;
    }

    protected abstract void registerMBean();

    protected abstract void unregisterMBean();

    protected synchronized void registerMBean(Object mbean, ObjectName objectName) {
        try {
            unregisterMBean(objectName);

            LOG.info("Registering {} to MBeanServer {}", objectName.getCanonicalName(), mBeanServer);

            mBeanServer.registerMBean(mbean, objectName);
        } catch (InstanceAlreadyExistsException ex) {
            LOG.error("MBean is already registered because {}", ExceptionUtils.getStackTrace(ex));
        } catch (MBeanRegistrationException ex) {
            LOG.error("Can't register MBean because {}", ExceptionUtils.getStackTrace(ex));
        } catch (NotCompliantMBeanException ex) {
            LOG.error("MBean is not compliant MBean, Stopping registration because {}", ExceptionUtils.getStackTrace(ex));
        }
    }

    protected synchronized void unregisterMBean(ObjectName objectName) {
        try {
            if (mBeanServer.isRegistered(objectName)) {
                LOG.info("Unregistering {} to MBeanServer {}", objectName.getCanonicalName(), mBeanServer);

                mBeanServer.unregisterMBean(objectName);
            }
        } catch (MBeanRegistrationException ex) {
            LOG.error("Can't register MBean because {}", ExceptionUtils.getStackTrace(ex));
        } catch (InstanceNotFoundException ex) {
            LOG.error("MBean doesn't exist in the repository because {}", ExceptionUtils.getStackTrace(ex));
        }
    }

    protected ObjectName getObjectNameForBean(String camelContextName, String name) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(DOMAIN_NAME).append(":");
        buffer.append(KEY_BEAN + "=" + "java-" + camelContextName + ",");
        buffer.append(KEY_TYPE + "=" + TYPE_BEAN + ",");
        buffer.append(KEY_NAME + "=").append(ObjectName.quote(name));

        return createObjectName(buffer);
    }

    protected ObjectName createObjectName(StringBuilder buffer) {
        ObjectName answer = null;

        try {
            answer = new ObjectName(buffer.toString());
        } catch (MalformedObjectNameException ex) {
            LOG.error("Could not create ObjectName from {} because {}", buffer.toString(), ExceptionUtils.getStackTrace(ex));
        }

        return answer;
    }
}
