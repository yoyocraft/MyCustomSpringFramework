package com.juzi.spring.context;

import com.juzi.spring.component.MonsterDAO;
import com.juzi.spring.component.MonsterService;
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

        MonsterDAO monsterDAO = (MonsterDAO) mySpringContext.getBean("monsterDAO");
        System.out.println("monsterDAO = " + monsterDAO);
        MonsterDAO monsterDAO2 = (MonsterDAO) mySpringContext.getBean("monsterDAO");
        System.out.println("monsterDAO = " + monsterDAO2);

        MonsterService monsterService = (MonsterService) mySpringContext.getBean("monsterService");
        System.out.println("monsterService = " + monsterService);
        MonsterService monsterService2 = (MonsterService) mySpringContext.getBean("monsterService");
        System.out.println("monsterService2 = " + monsterService2);
    }

}