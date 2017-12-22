/**
 * Copyright 2009-2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.executor;

/**
 * @author Clinton Begin
 * 设计模式：单例(线程范围内)、类builder--链式
 * 变量共享：threadlocal机制。
 *（threadlocal机制--非阻塞，synchronized 阻塞式。）
 *
 * 引申：
 * http://blog.csdn.net/liusong0605/article/details/7975623
 *  ThreadLocal并不能替代同步机制，两者面向的问题领域不同。
 *  如果需要进行多个线程之间进行通信，则使用同步机制synchronized；
 *  如果需要隔离多个线程之间的共享冲突，可以使用threadLocal。这将极大的简化程序，使程序更加易读、简洁。
 *    ThreadLocal是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。
 *  在很多情况下，ThreadLocal比直接使用synchronized同步机制解决线程安全问题更简单，更方便，且结果程序拥有更高的并发性。
 *
 *  用户注册且对性能要求不高的场景使用synchronized更为合适，因为涉及到验证是否注册过；
 *  购物车是否加入物品（指非重复性）。
 */
public class ErrorContext {
    //换行符,功能和"\n"是一致的,但是此种写法屏蔽了 Windows和Linux的区别
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<ErrorContext>();


    private ErrorContext stored;
    private String resource;
    private String activity;
    private String object;
    private String message;
    private String sql;
    private Throwable cause;

    private ErrorContext() {
    }

    public static ErrorContext instance() {
        ErrorContext context = LOCAL.get();
        if (context == null) {
            context = new ErrorContext();
            LOCAL.set(context);
        }
        return context;
    }

    public ErrorContext store() {
        stored = this;
        LOCAL.set(new ErrorContext());
        return LOCAL.get();
    }

    public ErrorContext recall() {
        if (stored != null) {
            LOCAL.set(stored);
            stored = null;
        }
        return LOCAL.get();
    }

    public ErrorContext resource(String resource) {
        this.resource = resource;
        return this;
    }

    public ErrorContext activity(String activity) {
        this.activity = activity;
        return this;
    }

    public ErrorContext object(String object) {
        this.object = object;
        return this;
    }

    public ErrorContext message(String message) {
        this.message = message;
        return this;
    }

    public ErrorContext sql(String sql) {
        this.sql = sql;
        return this;
    }

    public ErrorContext cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public ErrorContext reset() {
        resource = null;
        activity = null;
        object = null;
        message = null;
        sql = null;
        cause = null;
        LOCAL.remove();
        return this;
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();

        // message
        if (this.message != null) {
            description.append(LINE_SEPARATOR);
            description.append("### ");
            description.append(this.message);
        }

        // resource
        if (resource != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error may exist in ");
            description.append(resource);
        }

        // object
        if (object != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error may involve ");
            description.append(object);
        }

        // activity
        if (activity != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error occurred while ");
            description.append(activity);
        }

        // activity
        if (sql != null) {
            description.append(LINE_SEPARATOR);
            description.append("### SQL: ");
            // http://blog.csdn.net/xiazdong/article/details/8145728 回车、换行、tab
            description.append(sql.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim());
        }

        // cause
        if (cause != null) {
            description.append(LINE_SEPARATOR);
            description.append("### Cause: ");
            description.append(cause.toString());
        }

        return description.toString();
    }

}
