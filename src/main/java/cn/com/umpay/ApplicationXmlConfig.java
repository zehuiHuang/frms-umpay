package cn.com.umpay;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = { "classpath:app-plugin.xml" })
public class ApplicationXmlConfig {

}
