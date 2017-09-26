package cn.com.umpay.frms.preprocessor.main;

import cn.com.umpay.frms.preprocessor.core.LogEventListener;
import javax.servlet.ServletContextEvent;
import org.drools.SystemEventListenerFactory;

import org.drools.io.ResourceFactory;
import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener;

public class ContextLoaderListener
  extends SpringContextLoaderListener
{
  public void contextInitialized(ServletContextEvent paramServletContextEvent)
  {
    SystemEventListenerFactory.setSystemEventListener(new LogEventListener());
    super.contextInitialized(paramServletContextEvent);
    ResourceFactory.getResourceChangeNotifierService().start();
    ResourceFactory.getResourceChangeScannerService().start();
  }
  
  public void contextDestroyed(ServletContextEvent paramServletContextEvent)
  {
    super.contextDestroyed(paramServletContextEvent);
    ResourceFactory.getResourceChangeNotifierService().stop();
    ResourceFactory.getResourceChangeScannerService().stop();
  }
}
