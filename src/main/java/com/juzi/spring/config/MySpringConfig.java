package com.juzi.spring.config;

import com.juzi.spring.annotation.ComponentScan;

/**
 * 作用：代替Spring中的*.xml文件
 * 指定要扫描的包的全路径
 *
 * @author codejuzi
 */
@ComponentScan(value = "com.juzi.spring.component")
public class MySpringConfig {
}
