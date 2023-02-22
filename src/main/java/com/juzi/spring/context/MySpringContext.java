package com.juzi.spring.context;

import com.juzi.spring.annotation.Component;
import com.juzi.spring.annotation.MyComponentScan;
import com.juzi.spring.annotation.Scope;
import com.juzi.spring.config.MySpringConfig;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URL;
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
    private Class<MySpringConfig> configClass;

    /**
     * 过滤出要处理的类名的后缀
     */
    private static final String PROCESS_FILE_PATH_SUFFIX = ".class";

    /**
     * 默认的scope value
     */
    private static final String DEFAULT_SCOPE_VALUE = "singleton";

    /**
     * 存放bean的定义
     */
    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap;

    /**
     * bean 单例池
     */
    private final ConcurrentHashMap<String, Object> singletonObjects;


    public MySpringContext(Class<MySpringConfig> configClass) {
        singletonObjects = new ConcurrentHashMap<>();
        beanDefinitionMap = new ConcurrentHashMap<>();
        beanDefinitionScan(configClass);
    }

    public void beanDefinitionScan(Class<MySpringConfig> configClass) {

        this.configClass = configClass;

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
        if(resourceFile.isDirectory()) {
            File[] files = resourceFile.listFiles();
            assert files != null;
            for (File file : files) {
                String fileAbsolutePath = file.getAbsolutePath();
                // 过滤出.class文件
                if(fileAbsolutePath.endsWith(PROCESS_FILE_PATH_SUFFIX)) {
                    // 获取到类名
                    int startIndex = fileAbsolutePath.lastIndexOf("/") + 1;
                    int endIndex = fileAbsolutePath.indexOf(PROCESS_FILE_PATH_SUFFIX);
                    String className = fileAbsolutePath.substring(startIndex, endIndex);
                    // 获取全类名（类的完整路径）
                    String classFullName = originPath + "." + className;

                    // 判断类是否是Spring Bean
                    try {
                        Class<?> clazz = classLoader.loadClass(classFullName);
                        if(clazz.isAnnotationPresent(Component.class)) {
                            // 封装bean信息到BeanDefinition
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            // 得到beanName
                            Component component = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();
                            // beanName校验
                            if("".equals(beanName)) {
                                beanName = StringUtils.uncapitalize(className);
                            }
                            // 得到bean的Scope
                            if(clazz.isAnnotationPresent(Scope.class)) {
                                // 有Scope注解
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScopeValue(scopeAnnotation.value());
                            } else {
                                // 无Scope注解
                                beanDefinition.setScopeValue(DEFAULT_SCOPE_VALUE);
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        } else {
                            System.out.printf("%s不是一个SpringBean%n", className);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
