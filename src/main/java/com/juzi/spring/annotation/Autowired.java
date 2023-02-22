package com.juzi.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * custom Autowired annotation
 *
 * @author codejuzi
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

    /**
     * 是否必须装配成功
     *
     * @return true or false
     */
    String required() default "true";
}
