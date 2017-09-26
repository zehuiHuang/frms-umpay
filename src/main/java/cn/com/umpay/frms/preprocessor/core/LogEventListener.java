package cn.com.umpay.frms.preprocessor.core;

import org.drools.SystemEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventListener
  implements SystemEventListener
{
  Logger MEjnWwjnJJ = LoggerFactory.getLogger(LogEventListener.class);
  
  public void info(String paramString)
  {
    this.MEjnWwjnJJ.debug(paramString);
  }
  
  public void info(String paramString, Object paramObject)
  {
    this.MEjnWwjnJJ.debug(paramString, paramObject);
  }
  
  public void warning(String paramString)
  {
    this.MEjnWwjnJJ.debug(paramString);
  }
  
  public void warning(String paramString, Object paramObject)
  {
    this.MEjnWwjnJJ.debug(paramString, paramObject);
  }
  
  public void exception(String paramString, Throwable paramThrowable)
  {
    this.MEjnWwjnJJ.error(paramString, paramThrowable);
  }
  
  public void exception(Throwable paramThrowable)
  {
    this.MEjnWwjnJJ.error(paramThrowable.getMessage(), paramThrowable);
  }
  
  public void debug(String paramString)
  {
    this.MEjnWwjnJJ.debug(paramString);
  }
  
  public void debug(String paramString, Object paramObject)
  {
    this.MEjnWwjnJJ.debug(paramString, paramObject);
  }
}
