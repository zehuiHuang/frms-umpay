package cn.com.umpay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.bsfit.frms.obj.MemCachedItem;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.umpay.loader.cal.AbstractUmpayRiskPayOrderCalculationTask;
import com.umpay.loader.cal.RiskPayOrderCalculaterHelper;
import com.umpay.loader.cal.impl.MerAccountTask;
import com.umpay.loader.pojo.RiskPayOrder;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

@Configuration
public class PreConfig
{
//  @Value("${rules.jdbc.type:oracle}")
//  private String jdbcType;
//  private DataSource rulesDataSource;
//  
//  @Bean
//  @ConfigurationProperties(prefix="rules.jdbc")//数据库根字段
//  public DataSource rulesDataSource()
//  {
//    return this.rulesDataSource = DataSourceBuilder.create().build();
//  }
//  
//  @Bean
//  public SqlSessionFactory rulesSqlSessionFactory()
//    throws Exception
//  {
//    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//    bean.setDataSource(this.rulesDataSource);
//    bean.setConfigLocation(new ClassPathResource("RulesMybatisConfig_" + this.jdbcType + ".xml"));
//    return bean.getObject();
//  }
//  
//  @Bean
//  public HttpMessageConverters customConverters()
//  {
//    FastJsonHttpMessageConverter fastjson = new FastJsonHttpMessageConverter();
//    return new HttpMessageConverters(new HttpMessageConverter[] { fastjson });
//  }
  //----------------------------------
  @Value("${pre.threadSize}")
  private int threadSize;
  @Bean
  public ExecutorService getExecutorService(){
	  return Executors.newFixedThreadPool(this.threadSize);
  }
 /* @Bean
  public RiskPayOrderCalculaterHelper  getRiskPayOrderCalculaterHelper(){
	 RiskPayOrderCalculaterHelper riskPayOrderCalculaterHelper =new RiskPayOrderCalculaterHelper();
	 
	 List<AbstractUmpayRiskPayOrderCalculationTask> calculationTasks= new ArrayList<AbstractUmpayRiskPayOrderCalculationTask>();
	 calculationTasks.add(getMerAccountTask());
	 riskPayOrderCalculaterHelper.setCalculationTasks(calculationTasks);

	 return  riskPayOrderCalculaterHelper;
  }
  @Bean
  public AbstractUmpayRiskPayOrderCalculationTask getAbstractUmpayRiskPayOrderCalculationTask(){
	  AbstractUmpayRiskPayOrderCalculationTask abstractUmpayRiskPayOrderCalculationTask =new AbstractUmpayRiskPayOrderCalculationTask() {
		@Override
		protected MemCachedItem riskPayOrder2MemCachedItem(RiskPayOrder payOrder) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected MemCachedItem initContext(MemCachedItem item) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected String getTaskName() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected String getIgnoreKeys() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected String[] contextKeys() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected List<MemCachedItem> calc(List<MemCachedItem> items,
				List<RiskPayOrder> payOrders) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	abstractUmpayRiskPayOrderCalculationTask.setCalcLog(true);
	abstractUmpayRiskPayOrderCalculationTask.setCalcTimeLog(false);
	abstractUmpayRiskPayOrderCalculationTask.setJedisPool(getJedisSentinelPool());
	return null;
  }
  @Bean
  public JedisPoolConfig  getJedisPoolConfig(){
	  JedisPoolConfig jedisPoolConfig= new JedisPoolConfig();
	  jedisPoolConfig.setMaxTotal(50);
	  jedisPoolConfig.setMaxIdle(50);
	  jedisPoolConfig.setMinIdle(50);
	  return jedisPoolConfig;
  }
  @Value("${redis.sentinel.name:frms}")
  private String name;
  
  @Value("${redis.sentinel.url.1}")
  private String url1;
  @Value("${redis.sentinel.url.2}")
  private String url2;
  @Value("${redis.sentinel.url.3}")
  private String url3;
  @Value("${redis.sentinel.url.4}")
  private String url4;
  @Bean
  public JedisSentinelPool getJedisSentinelPool(){
	  Set<String> set= new HashSet<String>();
	  set.add(this.url1);
	  set.add(this.url2);
	  set.add(this.url3);
	  set.add(this.url4);
	 JedisSentinelPool  jedisSentinelPool = new JedisSentinelPool(this.name,set,getJedisPoolConfig());
	  return jedisSentinelPool;
  }
  
 @Bean
  public MerAccountTask getMerAccountTask(){
	  MerAccountTask merAccountTask = new MerAccountTask();
	  return merAccountTask;
  }
 @Bean
 public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(){
	 ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
	 threadPoolTaskExecutor.setCorePoolSize(10);
	 threadPoolTaskExecutor.setMaxPoolSize(50);
	 return threadPoolTaskExecutor;
 }*/
}
