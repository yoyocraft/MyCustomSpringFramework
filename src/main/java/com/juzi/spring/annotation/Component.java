package com.juzi.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指明类是Spring bean的注解
 *
 * @author codejuzi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * 指定在ioc容器中的key值
     *
     * @return key
     */
    String value() default "";
}
