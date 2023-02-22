package com.juzi.spring.component;

import com.juzi.spring.annotation.Component;
import com.juzi.spring.processor.BeanPostProcessor;

import java.lang.reflect.Proxy;

/**
 * bean 后置处理器
 *
 * @author codejuzi
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    private static final String AOP_CASE_BEAN_NAME = "myCal";

    private static final String AOP_CASE_METHOD_NAME = "getSum";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.printf("postProcessBeforeInitialization is accessing, %s, bean = %s%n", beanName, bean.getClass().toString());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.printf("postProcessAfterInitialization is accessing, %s, bean = %s%n", beanName, bean.getClass().toString());
        // AOP CASE
        if(AOP_CASE_BEAN_NAME.equals(beanName)) {
            // 获取bean代理对象
            return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        String methodName = method.getName();
                        System.out.println("methodName = " + methodName);
                        Object result;
                        if (AOP_CASE_METHOD_NAME.equals(methodName)) {
                            MyCalAspect.before();
                            result = method.invoke(bean, args);
                            MyCalAspect.afterReturning();
                        } else {
                            result = method.invoke(bean, args);
                        }
                        return result;
                    });
        }
        // 不需要做AOP处理
        return bean;
    }
}
