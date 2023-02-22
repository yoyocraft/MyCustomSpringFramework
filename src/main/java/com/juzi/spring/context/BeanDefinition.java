package com.juzi.spring.context;

/**
 * bean definition
 *
 * @author codejuzi
 */
public class BeanDefinition {

    /**
     * bean 对应的class对象
     */
    private Class<?> clazz;

    /**
     * bean 作用域
     * - singleton
     * - prototype
     */
    private String scopeValue;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getScopeValue() {
        return scopeValue;
    }

    public void setScopeValue(String scopeValue) {
        this.scopeValue = scopeValue;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "clazz=" + clazz +
                ", scopeValue='" + scopeValue + '\'' +
                '}';
    }
}
