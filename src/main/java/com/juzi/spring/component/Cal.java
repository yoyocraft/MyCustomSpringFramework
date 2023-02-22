package com.juzi.spring.component;

/**
 * aop case interface
 *
 * @author codejuzi
 */
public interface Cal {
    /**
     * sum of num1 and num2
     *
     * @param num1 num1
     * @param num2 num2
     * @return sum
     */
    long getSum(int num1, int num2);

    /**
     * sub of num1 and num2
     *
     * @param num1 num1
     * @param num2 num2
     * @return sub
     */
    long getSub(int num1, int num2);
}
