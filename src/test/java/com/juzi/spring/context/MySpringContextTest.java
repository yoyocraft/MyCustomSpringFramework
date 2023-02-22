package com.juzi.spring.context;

import com.juzi.spring.component.MonsterDAO;
import com.juzi.spring.component.MonsterService;
import com.juzi.spring.config.MySpringConfig;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author codejuzi
 */
public class MySpringContextTest {

    @Test
    public void testBeanDefinition() {
        MySpringContext mySpringContext = new MySpringContext(MySpringConfig.class);

        Assert.assertNotNull(mySpringContext);

        MonsterDAO monsterDAO = (MonsterDAO) mySpringContext.getBean("monsterDAO");
        System.out.println("monsterDAO = " + monsterDAO);
        MonsterDAO monsterDAO2 = (MonsterDAO) mySpringContext.getBean("monsterDAO");
        System.out.println("monsterDAO = " + monsterDAO2);

        MonsterService monsterService = (MonsterService) mySpringContext.getBean("monsterService");
        System.out.println("monsterService = " + monsterService);
        MonsterService monsterService2 = (MonsterService) mySpringContext.getBean("monsterService");
        System.out.println("monsterService2 = " + monsterService2);
    }

    @Test
    public void testIOC() {
        MySpringContext mySpringContext = new MySpringContext(MySpringConfig.class);

        Assert.assertNotNull(mySpringContext);

        MonsterService monsterService = (MonsterService) mySpringContext.getBean("monsterService");
        monsterService.hi();
    }

    @Test
    public void testPostBeanProcessor() {
        MySpringContext mySpringContext = new MySpringContext(MySpringConfig.class);

        Assert.assertNotNull(mySpringContext);
    }

}