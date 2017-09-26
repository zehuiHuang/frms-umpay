package cn.com.umpay;

import java.io.PrintStream;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude={SecurityAutoConfiguration.class})//把需要的依赖jar包自动打包进来
//启动自动配置模式，在我们的配置中会将对Tomcat的依赖级联进来，因此在应用启动时将会自动启动一个嵌入式的Tomcat,也会自动注册所需的DispatcherServlet，这都不需要类似web.xml这样的配置
//其次就是将所有符合自动配置条件的bean定义、符合条件的@Configuration配置 加载到IoC容器
@ComponentScan //告知Spring扫描指定的包来初始化Spring Bean,确保我们声明的Bean能够被发现(其实就是自动扫描并加载符合条件的组件)
public class PreApp
{
//	static {
//    try{
//        //初始化log4j
//        String log4jPath = PreApp.class.getClassLoader().getResource("").getPath()+"log4j.properties";
//
//        System.out.println("初始化Log4j。。。。");
//
//        System.out.println("path is "+ log4jPath);
//
//        PropertyConfigurator.configure(log4jPath);
//
//    }catch (Exception e){
//
//        e.printStackTrace();
//
//    }
//
//}

  public static void main(String[] args)
  {
    try
    {
      new SpringApplicationBuilder(new Object[0]).showBanner(false).sources(new Class[] { PreApp.class }).run(args);
      //showBanner属性被设置为true，则打印banner
    }
    catch (BeanCreationException e)
    {
      System.err.println("请确认已正确配置该平台，请检查是否正确配置(数据库、测试插件等)");
      e.printStackTrace();
    }
  }
}
