# MyCustomSpringFramework
> This project use custom annotation and configuration class to simulate spring load bean to container.At the same time, it also provides more function, such as custom Aspect Oriented Programming(AOP) and BeanPostProcessor and so on.

## Spring 容器创建、初始化流程分析

1. 读取`beans.xml`文件
2. 扫描指定的包
   1. 扫描包，得到bean的class对象，同时排除包下不是bean的
   2. 将bean的信息封装成`BeanDefinition`对象，并存入Map
   3. 初始化单例池，也就是意味着要将所有单例对象创建并初始化存入单例池

=> 容器创建、初始化之后的结果

1. `BeanDefinition Map`集合：`key[beanName] - value[BeanDefinition Object]`
2. `SingletonBean Map`单例池：`key[beanName] - value[SingletonBean Object]`

## Spring 容器 getBean(name)实现机制

1. 执行`getBean(name)`
2. 首先到`BeanDefinition Map`获取Bean信息
3. 三种情况：
   1. bean不存在，抛出异常
   2. bean存在，并且是singleton => 从单例池中获取
   3. bean存在，并且是prototype => 从`BeanDefinition Map`中获取到bean的class对象，通过反射，创建bean对象并返回

## Spring 扩展功能

1. 依赖注入
2. Bean后置处理器机制
3. AOP机制

> AOP机制需要Bean后置处理器机制 + 动态代理机制
