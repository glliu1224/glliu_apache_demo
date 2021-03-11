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


package org.apache.catalina;

import java.io.File;

import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.catalina.startup.Catalina;

/**
 * A <code>Server</code> element represents the entire Catalina
 * servlet container.  Its attributes represent the characteristics of
 * the servlet container as a whole.  A <code>Server</code> may contain
 * one or more <code>Services</code>, and the top level set of naming
 * resources.
 * <p>
 * Normally, an implementation of this interface will also implement
 * <code>Lifecycle</code>, such that when the <code>start()</code> and
 * <code>stop()</code> methods are called, all of the defined
 * <code>Services</code> are also started or stopped.
 * <p>
 * In between, the implementation must open a server socket on the port number
 * specified by the <code>port</code> property.  When a connection is accepted,
 * the first line is read and compared with the specified shutdown command.
 * If the command matches, shutdown of the server is initiated.
 * <p>
 * <strong>NOTE</strong> - The concrete implementation of this class should
 * register the (singleton) instance with the <code>ServerFactory</code>
 * class in its constructor(s).
 *
 * @author Craig R. McClanahan
 */

/**
 * servlet容器。它的属性代表了servlet容器作为一个整体，基本相当于Tomcat
 * 服务器可能包含一个或多个Service，以及顶级命名集资源。
 * 通常，此接口的实现也将实现Lifecycle
 * 调用stop（）方法，所有已定义的Service（web应用）也会停止
 * 在这两者之间，必须自定义不同的端口,由port属性指定.
 * 当连接被接受时，读取第一行并与指定的shutdown命令进行比较，如果命令匹配，则启动服务器关闭
 * 这个类的具体实现应该向ServerFactory注册（singleton）实例，在其构造函数中初始化。
 */
public interface Server extends Lifecycle {

    // ------------------------------------------------------------- Properties

    /**
     * @return 全局命名资源.
     */
    public NamingResourcesImpl getGlobalNamingResources();


    /**
     * 设置全局命名资源.
     *
     * @param globalNamingResources 全局命名资源入参
     */
    public void setGlobalNamingResources
        (NamingResourcesImpl globalNamingResources);


    /**
     * @return 获取全局命名资源
     */
    public javax.naming.Context getGlobalNamingContext();


    /**
     * @return 获取端口号
     */
    public int getPort();


    /**
     * 设置端口号
     *
     * @param port 入参端口号
     */
    public void setPort(int port);


    /**
     * @return 我们监听关机命令的地址
     * 获取地址
     */
    public String getAddress();


    /**
     * 设置IP
     * @param address The new address
     */
    public void setAddress(String address);


    /**
     * @return 我们正在等待的关机命令字符串（SHUTDOWN）
     */
    public String getShutdown();


    /**
     * Set the shutdown command we are waiting for.
     *
     * @param shutdown The new shutdown command
     */
    public void setShutdown(String shutdown);


    /**
     * @return the parent class loader for this component. If not set, return
     * {@link #getCatalina()} {@link Catalina#getParentClassLoader()}. If
     * catalina has not been set, return the system class loader.
     */
    /**
     * 此组件的父类加载器，如果未设置，则返回getCatalina(),如果Catalina未设置，返回系统类加载器
     * @return
     */
    public ClassLoader getParentClassLoader();


    /**
     * 设置父类加载器
     */
    public void setParentClassLoader(ClassLoader parent);


    /**
     * @return 外部Catalina启动/关闭组件（如果存在）
     */
    public Catalina getCatalina();

    /**
     * Set the outer Catalina startup/shutdown component if present.
     *设置外部Catalina组件启动/关闭(如果存在)
     * @param catalina the outer Catalina component
     */
    public void setCatalina(Catalina catalina);


    /**
     * @return the configured base (instance) directory. Note that home and base
     * may be the same (and are by default). If this is not set the value
     * returned by {@link #getCatalinaHome()} will be used.
     */
    /**
     * 配置的基本（实例）目录。注意home和base可能相同（默认情况下是）。如果未设置此值，返回getCatalinaHome()
     * @return
     */
    public File getCatalinaBase();

    /**
     * Set the configured base (instance) directory. Note that home and base
     * may be the same (and are by default).
     *
     * @param catalinaBase the configured base directory
     */
    public void setCatalinaBase(File catalinaBase);


    /**
     * @return the configured home (binary) directory. Note that home and base
     * may be the same (and are by default).
     */
    public File getCatalinaHome();

    /**
     * Set the configured home (binary) directory. Note that home and base
     * may be the same (and are by default).
     *
     * @param catalinaHome the configured home directory
     */
    public void setCatalinaHome(File catalinaHome);


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new Service to the set of defined Services.
     *向已经定义的服务集群添加一个服务
     * @param service The Service to be added
     */
    public void addService(Service service);


    /**
     * Wait until a proper shutdown command is received, then return.
     * 等待直到收到正确的关机命令，然后返回
     */
    public void await();


    /**
     * Find the specified Service
     *
     * @param name Name of the Service to be returned
     * @return the specified Service, or <code>null</code> if none exists.
     * 根据服务名查询服务，如果存在返回服务，不存在返回null
     */
    public Service findService(String name);


    /**
     * @return the set of Services defined within this Server.
     * 查询当前容器中所有服务
     */
    public Service[] findServices();


    /**
     * Remove the specified Service from the set associated from this
     * Server.
     *
     * @param service The Service to be removed
     * 删除一个服务
     */
    public void removeService(Service service);


    /**
     * @return the token necessary for operations on the associated JNDI naming
     * context.
     * 获取对关联JNDI命名进行操作所需的token
     */
    public Object getNamingToken();
}
