package com.juzi.spring.context;

import com.juzi.spring.annotation.Component;
import com.juzi.spring.annotation.MyComponentScan;
import com.juzi.spring.config.MySpringConfig;

import java.io.File;
import java.net.URL;

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

    private static final String PROCESS_FILE_PATH_SUFFIX = ".class";

    public MySpringContext(Class<MySpringConfig> configClass) {
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
                            System.out.printf("%s是一个SpringBean%n", className);
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
