/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 责任链模式，通过动态代理组织多个拦截器（插件），
 * 通过这些拦截器可以改变Mybatis的默认行为（诸如SQL重写之类的），由于插件会深入到Mybatis的核心
 *
 *
 * @author Clinton Begin
 * 重构
 */
public class InterceptorChain {

  private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

    /**
     *  泛型<T>和object重要区别之一便是：使用泛型时，多是参数类型和返回值类有相关性；object没有
     *  此方法采用object 便是target类型与返回值类型没有相关性
     * @param target 被拦截的对象
     * @return can be anthing.
     */
  public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
      target = interceptor.plugin(target);
    }
    return target;
  }

  public void addInterceptor(Interceptor interceptor) {
    interceptors.add(interceptor);
  }

    /**
     * yang
     * https://blog.csdn.net/cilen/article/details/7744969
     * 重构——改善既有代码的设计
     * encapsulate collection 只读
     * 场景：
     *      防止用户不知情的情况下数据被篡改
     */
  public List<Interceptor> getInterceptors() {
    return Collections.unmodifiableList(interceptors);
  }

}
