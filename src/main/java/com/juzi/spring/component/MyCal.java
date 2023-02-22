package com.juzi.spring.component;


import com.juzi.spring.annotation.Component;

/**
 * implement of cal
 *
 * @author codejuzi
 */
@Component(value = "myCal")
public class MyCal implements Cal {

    @Override
    public long getSum(int num1, int num2) {
        int result = num1 + num2;
        System.out.println("in method getSum => result = " + result);
        return result;
    }

    @Override
    public long getSub(int num1, int num2) {
        int result = num1 - num2;
        System.out.println("in method getSum => result = " + result);
        return result;
    }
}
