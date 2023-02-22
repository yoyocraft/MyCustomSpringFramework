package com.juzi.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * custom scope annotation
 *
 * @author codejuzi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    /**
     * - singleton
     * - prototype
     *
     * @return 类型
     */
    String value();
}
