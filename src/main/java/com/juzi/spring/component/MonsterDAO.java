package com.juzi.spring.component;

import com.juzi.spring.annotation.Component;
import com.juzi.spring.annotation.Scope;

/**
 * DAO Component
 * @author codejuzi
 */
@Scope(value = "prototype")
@Component(value = "monsterDAO")
public class MonsterDAO {
}
