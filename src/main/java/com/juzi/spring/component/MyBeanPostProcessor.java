package com.juzi.spring.component;

import com.juzi.spring.annotation.Component;
import com.juzi.spring.processor.BeanPostProcessor;

/**
 * bean 后置处理器
 *
 * @author codejuzi
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.printf("postProcessBeforeInitialization is accessing, %s, bean = %s%n", beanName, bean.getClass().toString());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.printf("postProcessAfterInitialization is accessing, %s, bean = %s%n", beanName, bean.getClass().toString());
        return bean;
    }
}
