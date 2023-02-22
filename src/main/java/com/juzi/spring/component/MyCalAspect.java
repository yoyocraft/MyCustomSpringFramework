package com.juzi.spring.component;

/**
 * MyCal aspect class
 *
 * @author codejuzi
 */
public class MyCalAspect {

    public static void before() {
        System.out.println("before method ......");
    }

    public static void afterReturning() {
        System.out.println("after returning method ......");
    }
}
