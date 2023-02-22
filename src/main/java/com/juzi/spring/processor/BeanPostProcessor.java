package com.juzi.spring.processor;

/**
 * bean后置处理器接口
 *
 * @author codejuzi
 */
public interface BeanPostProcessor {

    /**
     * bean初始化方法前调用
     *
     * @param bean     bean
     * @param beanName bean's name
     * @return processed bean
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * bean初始化方法后调用
     *
     * @param bean     bean
     * @param beanName bean's name
     * @return processed bean
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
