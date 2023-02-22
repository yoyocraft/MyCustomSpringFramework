package com.juzi.spring.context;

import com.juzi.spring.annotation.Autowired;
import com.juzi.spring.annotation.Component;
import com.juzi.spring.annotation.MyComponentScan;
import com.juzi.spring.annotation.Scope;
import com.juzi.spring.config.MySpringConfig;
import com.juzi.spring.processor.BeanPostProcessor;
import com.juzi.spring.processor.InitializingBean;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 容器类
 *
 * @author codejuzi
 */
public class MySpringContext {
    /**
     * 保存配置类对象，获取扫描包
     */
    private final Class<MySpringConfig> configClass;

    /**
     * 过滤出要处理的类名的后缀
     */
    private static final String PROCESS_FILE_PATH_SUFFIX = ".class";

    /**
     * 默认的scope value
     */
    private static final String DEFAULT_SCOPE_VALUE = "singleton";

    /**
     * autowired required
     */
    private static final String AUTOWIRED_REQUIRED = "true";

    /**
     * 存放bean的定义
     */
    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap;

    /**
     * bean 单例池
     */
    private final ConcurrentHashMap<String, Object> singletonObjects;

    /**
     * 存放bean的后置处理器
     */
    private final List<BeanPostProcessor> beanPostProcessorList;

    public MySpringContext(Class<MySpringConfig> configClass) {
        this.configClass = configClass;
        this.singletonObjects = new ConcurrentHashMap<>();
        this.beanDefinitionMap = new ConcurrentHashMap<>();
        this.beanPostProcessorList = new ArrayList<>();
        initBeanDefinitionMap();
        initSingletonObjects();
    }

    /**
     * 扫描包，初始化Bean Definition Map
     */
    public void initBeanDefinitionMap() {

        // 获取扫描包
        MyComponentScan myComponentScan = this.configClass.getDeclaredAnnotation(MyComponentScan.class);
        String originPath = myComponentScan.value();

        // 得到类加载器
        ClassLoader classLoader = this.getClass().getClassLoader();

        // 得到扫描包的资源路径
        String proPath = originPath.replaceAll("\\.", "/");
        URL resource = classLoader.getResource(proPath);

        // 遍历需要加载的资源路径下的所有文件
        assert resource != null;
        File resourceFile = new File(resource.getFile());
        if (resourceFile.isDirectory()) {
            File[] files = resourceFile.listFiles();
            assert files != null;
            for (File file : files) {
                String fileAbsolutePath = file.getAbsolutePath();
                // 过滤出.class文件
                if (fileAbsolutePath.endsWith(PROCESS_FILE_PATH_SUFFIX)) {
                    // 获取到类名
                    int startIndex = fileAbsolutePath.lastIndexOf("/") + 1;
                    int endIndex = fileAbsolutePath.indexOf(PROCESS_FILE_PATH_SUFFIX);
                    String className = fileAbsolutePath.substring(startIndex, endIndex);
                    // 获取全类名（类的完整路径）
                    String classFullName = originPath + "." + className;

                    // 判断类是否是Spring Bean
                    try {
                        Class<?> clazz = classLoader.loadClass(classFullName);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            // 判断是否是后置处理器
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                // 创建一个实例对象存入后置处理器集合，此处简化，不再放入单例池
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.newInstance();
                                beanPostProcessorList.add(beanPostProcessor);
                                continue;
                            }
                            // 封装bean信息到BeanDefinition
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            // 得到beanName
                            Component component = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();
                            // beanName校验
                            if ("".equals(beanName)) {
                                beanName = StringUtils.uncapitalize(className);
                            }
                            // 得到bean的Scope
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                // 有Scope注解
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScopeValue(scopeAnnotation.value());
                            } else {
                                // 无Scope注解
                                beanDefinition.setScopeValue(DEFAULT_SCOPE_VALUE);
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 通过beanDefinitionMap 初始化单例池Singleton Objects
     */
    public void initSingletonObjects() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            // 得到beanName
            String beanName = beanDefinitionEntry.getKey();
            // 通过beanName得到BeanDefinition
            BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
            // 得到scope value
            String scopeValue = beanDefinition.getScopeValue();
            if (DEFAULT_SCOPE_VALUE.equals(scopeValue)) {
                // 单例，初始化bean实例存入单例池
                Object bean = this.createBean(beanName, beanDefinition);
                this.singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     * 根据BeanDefinition创建Bean
     *
     * @param beanName       bean's name
     * @param beanDefinition bean 信息
     * @return bean
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        // 得到bean的class对象
        Class<?> clazz = beanDefinition.getClazz();

        Object instance = null;

        // 通过反射创建bean实例
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            // 实现依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    String fieldName = declaredField.getName();
                    // 装配有Autowired注解
                    Autowired autowired = declaredField.getDeclaredAnnotation(Autowired.class);
                    String required = autowired.required();

                    // 必须装配成功 && bean definition map 中不存在该bean
                    if (AUTOWIRED_REQUIRED.equals(required) && !beanDefinitionMap.containsKey(fieldName)) {
                        throw new NullPointerException(fieldName + " is not exist in bean definition map");
                    }
                    // 得到对象并组装
                    Object wiredBean = getBean(fieldName);
                    declaredField.setAccessible(true);
                    declaredField.set(instance, wiredBean);
                }
            }
            instance = beanInitializingWithBeanPostProcessor(beanName, instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * bean初始化方法调用
     * bean后置处理器调用
     *
     * @param beanName bean's name
     * @param bean bean 实例
     * @return processed bean
     */
    private Object beanInitializingWithBeanPostProcessor(String beanName, Object bean) {
        System.out.println("-------------------------------------------------------");
        // 调用初始化方法前，调用bean的后置处理器
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            if (current != null) {
                bean = current;
            }
        }
        // 判断instance是否实现了InitializingBean接口
        if (bean instanceof InitializingBean) {
            try {
                // 调用初始化方法
                ((InitializingBean) bean).afterPropertiesSet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 调用初始化方法之后，调用bean的后置处理器
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            Object current = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            if (current != null) {
                bean = current;
            }
        }
        System.out.println("------------------------------------------------------");
        return bean;
    }

    /**
     * 根据beanName获取bean实例
     *
     * @param beanName bean's name in bean definition map
     * @return bean实例
     */
    public Object getBean(String beanName) {
        // bean不存在，抛出异常
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException(String.format("%s is not exist in bean definition map", beanName));
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        String scopeValue = beanDefinition.getScopeValue();
        if (DEFAULT_SCOPE_VALUE.equals(scopeValue)) {
            // bean存在，且为singleton，从单例池返回
            return singletonObjects.get(beanName);
        } else {
            // bean存在，且为prototype，创建实例返回
            return createBean(beanName, beanDefinition);
        }
    }
}
