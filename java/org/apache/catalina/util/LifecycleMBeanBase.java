/*
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

package org.apache.catalina.util;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.catalina.Globals;
import org.apache.catalina.JmxEnabled;
import org.apache.catalina.LifecycleException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.res.StringManager;

public abstract class LifecycleMBeanBase extends LifecycleBase
        implements JmxEnabled {

    private static final Log log = LogFactory.getLog(LifecycleMBeanBase.class);

    private static final StringManager sm =
        StringManager.getManager("org.apache.catalina.util");


    /* Cache components of the MBean registration. */
    /*缓存MBean注册的组件*/
    private String domain = null;
    private ObjectName oname = null;
    protected MBeanServer mserver = null;

    /**
     * Sub-classes wishing to perform additional initialization should override
     * this method, ensuring that super.initInternal() is the first call in the
     * overriding method.
     */
    /**
     * 希望执行额外初始化的子类应该重写
     * 这种方法，保证父类initInternal（）是在所有重写的方法中是系统中的第一个调用
     * @throws LifecycleException
     */
    @Override
    protected void initInternal() throws LifecycleException {
        // 如果oname不为null，那么注册已经通过
        // preRegister().
        if (oname == null) {
            mserver = Registry.getRegistry(null, null).getMBeanServer();

            oname = register(this, getObjectNameKeyProperties());
        }
    }


    /**
     * Sub-classes wishing to perform additional clean-up should override this
     * method, ensuring that super.destroyInternal() is the last call in the
     * overriding method.
     */
    /**
     * 希望执行额外初始化的子类重新此方法
     * 这个方法，保证父类destroyInternal()是所有重写的方法中系统最后一个调用此方法
     * @throws LifecycleException
     */
    @Override
    protected void destroyInternal() throws LifecycleException {
        unregister(oname);
    }


    /**
     * Specify the domain under which this component should be registered. Used
     * with components that cannot (easily) navigate the component hierarchy to
     * determine the correct domain to use.
     */
    /**
     * 指定此组件应在其下注册的域
     * 使用无法（轻松）导航到组件层次结构的组件，确定要使用的正确域
     * @param domain The name of the domain under which this component should be
     */
    @Override
    public final void setDomain(String domain) {
        this.domain = domain;
    }


    /**
     * Obtain the domain under which this component will be / has been
     * registered.
     */
    @Override
    public final String getDomain() {
        if (domain == null) {
            domain = getDomainInternal();
        }

        if (domain == null) {
            domain = Globals.DEFAULT_MBEAN_DOMAIN;
        }

        return domain;
    }


    /**
     * Method implemented by sub-classes to identify the domain in which MBeans
     * should be registered.
     *
     * @return  The name of the domain to use to register MBeans.
     */
    protected abstract String getDomainInternal();


    /**
     * Obtain the name under which this component has been registered with JMX.
     */
    @Override
    public final ObjectName getObjectName() {
        return oname;
    }


    /**
     * Allow sub-classes to specify the key properties component of the
     * {@link ObjectName} that will be used to register this component.
     * 注册组件方法
     * @return  The string representation of the key properties component of the
     *          desired {@link ObjectName}
     */
    protected abstract String getObjectNameKeyProperties();


    /**
     * Utility method to enable sub-classes to easily register additional
     * components that don't implement {@link JmxEnabled} with an MBean server.
     * <br>
     * Note: This method should only be used once {@link #initInternal()} has
     * been called and before {@link #destroyInternal()} has been called.
     *
     * @param obj                       The object the register
     * @param objectNameKeyProperties   The key properties component of the
     *                                  object name to use to register the
     *                                  object
     *
     * @return  The name used to register the object
     */
    /**
     * 使子类能够轻松地注册附加不使用MBean服务器实现{@link JmxEnabled}的组件
     * @param obj 寄存器中的对象
     * @param objectNameKeyProperties 用于注册对象
     * @return
     */
    protected final ObjectName register(Object obj,
            String objectNameKeyProperties) {

        // Construct an object name with the right domain
        StringBuilder name = new StringBuilder(getDomain());
        name.append(':');
        name.append(objectNameKeyProperties);

        ObjectName on = null;

        try {
            on = new ObjectName(name.toString());
            Registry.getRegistry(null, null).registerComponent(obj, on, null);
        } catch (Exception e) {
            log.warn(sm.getString("lifecycleMBeanBase.registerFail", obj, name), e);
        }

        return on;
    }


    /**
     * Utility method to enable sub-classes to easily unregister additional
     * components that don't implement {@link JmxEnabled} with an MBean server.
     * <br>
     * Note: This method should only be used once {@link #initInternal()} has
     * been called and before {@link #destroyInternal()} has been called.
     *
     * @param on    The name of the component to unregister
     */
    /**
     * 实用方法，使子类可以轻松地注销其他子类
     * 不使用MBean服务器实现{@link JmxEnabled}的组件
     * 注意：此方法只能在{@link#initInternal（）}完成后使用
     * 在调用{@link#destroyInternal（）}之前。
     * @param on
     */
    protected final void unregister(ObjectName on) {

        // If null ObjectName, just return without complaint
        if (on == null) {
            return;
        }

        // If the MBeanServer is null, log a warning & return
        if (mserver == null) {
            log.warn(sm.getString("lifecycleMBeanBase.unregisterNoServer", on));
            return;
        }

        try {
            mserver.unregisterMBean(on);
        } catch (MBeanRegistrationException e) {
            log.warn(sm.getString("lifecycleMBeanBase.unregisterFail", on), e);
        } catch (InstanceNotFoundException e) {
            log.warn(sm.getString("lifecycleMBeanBase.unregisterFail", on), e);
        }

    }


    /**
     * Not used - NOOP.
     */
    @Override
    public final void postDeregister() {
        // NOOP
    }


    /**
     * Not used - NOOP.
     */
    @Override
    public final void postRegister(Boolean registrationDone) {
        // NOOP
    }


    /**
     * Not used - NOOP.
     */
    @Override
    public final void preDeregister() throws Exception {
        // NOOP
    }


    /**
     * Allows the object to be registered with an alternative
     * {@link MBeanServer} and/or {@link ObjectName}.
     */
    @Override
    public final ObjectName preRegister(MBeanServer server, ObjectName name)
            throws Exception {

        this.mserver = server;
        this.oname = name;
        this.domain = name.getDomain().intern();

        return oname;
    }

}
