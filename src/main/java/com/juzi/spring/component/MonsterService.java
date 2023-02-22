package com.juzi.spring.component;

import com.juzi.spring.annotation.Autowired;
import com.juzi.spring.annotation.Component;

/**
 * Service Component
 * @author codejuzi
 */
@Component(value = "monsterService")
public class MonsterService {

    @Autowired
    private MonsterDAO monsterDAO;

    public void hi() {
        monsterDAO.hi();
    }
}
