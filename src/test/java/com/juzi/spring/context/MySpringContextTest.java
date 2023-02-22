package com.juzi.spring.context;

import com.juzi.spring.config.MySpringConfig;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author codejuzi
 */
public class MySpringContextTest {

    @Test
    public void testIOC() {
        MySpringContext mySpringContext = new MySpringContext(MySpringConfig.class);
    }

}