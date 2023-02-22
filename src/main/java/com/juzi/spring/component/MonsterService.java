package com.juzi.spring.component;

import com.juzi.spring.annotation.Autowired;
import com.juzi.spring.annotation.Component;
import com.juzi.spring.processor.InitializingBean;

/**
 * Service Component
 * @author codejuzi
 */
@Component(value = "monsterService")
public class MonsterService implements InitializingBean {

    @Autowired
    private MonsterDAO monsterDAO;

    public void hi() {
        monsterDAO.hi();
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("MonsterService Initializing Method is accessing ~~");
    }
}
