package com.juzi.spring.context;

import com.juzi.spring.component.Cal;
import com.juzi.spring.config.MySpringConfig;
import com.juzi.spring.context.MySpringContext;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test aop case
 *
 * @author codejuzi
 */
public class MyCalTest {

    @Test
    public void test() {
        MySpringContext mySpringContext = new MySpringContext(MySpringConfig.class);

        Assert.assertNotNull(mySpringContext);

        Cal myCal = (Cal) mySpringContext.getBean("myCal");
        myCal.getSum(10, 2);

        myCal.getSub(10, 2);
    }
}