package com.juzi.spring.component;

import com.juzi.spring.annotation.Component;
import com.juzi.spring.annotation.Scope;
import com.juzi.spring.processor.InitializingBean;

/**
 * DAO Component
 * @author codejuzi
 */
@Scope(value = "prototype")
@Component(value = "monsterDAO")
public class MonsterDAO implements InitializingBean {

    public void hi() {
        System.out.println("MonsterDAO - hi() - accessed");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("MonsterDAO Initializing Method is accessing ~~");
    }
}
