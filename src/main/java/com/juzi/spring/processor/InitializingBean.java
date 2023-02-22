package com.juzi.spring.processor;

/**
 * 初始化bean接口
 *
 * @author codejuzi
 */
public interface InitializingBean {

    /**
     * bean初始化方法
     *
     * @throws Exception initializing method exception
     */
    void afterPropertiesSet() throws Exception;
}
