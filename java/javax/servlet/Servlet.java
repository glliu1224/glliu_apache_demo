/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.servlet;

import java.io.IOException;

/**
 * Defines methods that all servlets must implement.
 *
 * <p>
 * A servlet is a small Java program that runs within a Web server. Servlets
 * receive and respond to requests from Web clients, usually across HTTP, the
 * HyperText Transfer Protocol.
 *
 * <p>
 * To implement this interface, you can write a generic servlet that extends
 * <code>javax.servlet.GenericServlet</code> or an HTTP servlet that extends
 * <code>javax.servlet.http.HttpServlet</code>.
 *
 * <p>
 * This interface defines methods to initialize a servlet, to service requests,
 * and to remove a servlet from the server. These are known as life-cycle
 * methods and are called in the following sequence:
 * <ol>
 * <li>The servlet is constructed, then initialized with the <code>init</code>
 * method.
 * <li>Any calls from clients to the <code>service</code> method are handled.
 * <li>The servlet is taken out of service, then destroyed with the
 * <code>destroy</code> method, then garbage collected and finalized.
 * </ol>
 *
 * <p>
 * In addition to the life-cycle methods, this interface provides the
 * <code>getServletConfig</code> method, which the servlet can use to get any
 * startup information, and the <code>getServletInfo</code> method, which allows
 * the servlet to return basic information about itself, such as author,
 * version, and copyright.
 *
 * @see GenericServlet
 * @see javax.servlet.http.HttpServlet
 */

/**
 * 此接口主要定义类servlet的生命周期方法
 * 定义了所有servlet必须实现的方法
 * Servlet是在Web服务器中运行的小型JAVA程序，Servlet接收和响应来自Web客户端的请求，通常是通过HTTP超文本传输协议
 * 为了实现这个接口，可以编写一个扩展javax.servlet.GenericServlet或扩展javax.servlet.http.HttpServlet
 *
 * 此接口定义了初始化servlet、服务请求的方法，以及从服务器中删除servlet，这些被称为生命周期，方法和调用顺序如下
 * 构建servlet，然后调用init()初始化->处理客户端对service()方法的任何调用->servlet停止服务，然后调用destroy()销毁->垃圾收集并最终确定
 * 除了生命周期方法外，此接口还提供getServletConfig()方法，servlet可以使用该方法获取任何启动信息，以及getServletInfo方法返回servlet关于自身的基本信息
 *
 */
public interface Servlet {


    /**
     * 由servlet容器调用，以向servlet指示正在投入使用
     * servlet容器只调用init()方法一次，在实例化servlet之后，init方法必须在servlet可以接收任何请求之前成功完成
     * 如果init方法抛出一个ServletException不会在Web服务器定义的时间段内返回
     * * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException;

    /**
     *
     * Returns a {@link ServletConfig} object, which contains initialization and
     * startup parameters for this servlet. The <code>ServletConfig</code>
     * object returned is the one passed to the <code>init</code> method.
     *
     * <p>
     * Implementations of this interface are responsible for storing the
     * <code>ServletConfig</code> object so that this method can return it. The
     * {@link GenericServlet} class, which implements this interface, already
     * does this.
     *
     * @return the <code>ServletConfig</code> object that initializes this
     *         servlet
     *
     * @see #init
     */
    /**
     *返回一个ServletConfig对象，其中包含初始化此servlet的启动参数，Servlet返回的对象是传递给init方法对象
     * 这个接口的实现负责存储ServletConfig对象，以便此方法可以返回它，这个GenericServlet类已经实现了这个接口
     * @return
     */
    public ServletConfig getServletConfig();

    /**
     * Called by the servlet container to allow the servlet to respond to a
     * request.
     *
     * <p>
     * This method is only called after the servlet's <code>init()</code> method
     * has completed successfully.
     *
     * <p>
     * The status code of the response always should be set for a servlet that
     * throws or sends an error.
     *
     *
     * <p>
     * Servlets typically run inside multithreaded servlet containers that can
     * handle multiple requests concurrently. Developers must be aware to
     * synchronize access to any shared resources such as files, network
     * connections, and as well as the servlet's class and instance variables.
     * More information on multithreaded programming in Java is available in <a
     * href
     * ="http://java.sun.com/Series/Tutorial/java/threads/multithreaded.html">
     * the Java tutorial on multi-threaded programming</a>.
     *
     *
     * @param req
     *            the <code>ServletRequest</code> object that contains the
     *            client's request
     *
     * @param res
     *            the <code>ServletResponse</code> object that contains the
     *            servlet's response
     *
     * @exception ServletException
     *                if an exception occurs that interferes with the servlet's
     *                normal operation
     *
     * @exception IOException
     *                if an input or output exception occurs
     */
    /**
     * 由servlet容器调用以允许servlet响应请求
     * 此方法仅在Servlet的init方法调用之后调用。
     * 响应的状态代码始终应设置为Servlet抛出或发送错误
     * Servlet通常在多线程servlet容器中运行，这些容器可以同时处理多个请求，开发人员必须意识到同步访问任何共享资源，如文件、网络以及servlet的类和实例变量
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException;

    /**
     * Returns information about the servlet, such as author, version, and
     * copyright.
     *
     * <p>
     * The string that this method returns should be plain text and not markup
     * of any kind (such as HTML, XML, etc.).
     *
     * @return a <code>String</code> containing servlet information
     */
    /**
     * 返回与servlet相关的基本信息，比如作者、版本
     * @return
     */
    public String getServletInfo();

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being taken out of service. This method is only called once all
     * threads within the servlet's <code>service</code> method have exited or
     * after a timeout period has passed. After the servlet container calls this
     * method, it will not call the <code>service</code> method again on this
     * servlet.
     *
     * <p>
     * This method gives the servlet an opportunity to clean up any resources
     * that are being held (for example, memory, file handles, threads) and make
     * sure that any persistent state is synchronized with the servlet's current
     * state in memory.
     */
    /**
     *由servlet容器调用，以向servlet指示正在停止使用，此方法只调用一次，servlet的service方法中的线程已退出或超时时间过后，
     * 在servlet容器中调用此方法，它将不会在此基础上再次调用service方法
     */
    public void destroy();
}
