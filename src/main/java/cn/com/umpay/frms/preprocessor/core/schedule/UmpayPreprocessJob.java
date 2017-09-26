package cn.com.umpay.frms.preprocessor.core.schedule;

import cn.com.umpay.drools.FastKnowledgeAgent;
import cn.com.umpay.drools.KnowledgeAgentPool;
import cn.com.bsfit.frms.base.func.timed.incr.SumAccumulateFunction;
import cn.com.bsfit.frms.base.load.IncrPayOrder;
import cn.com.bsfit.frms.base.load.TransformPayOrderToMemcachedItem;
import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.bsfit.frms.obj.TimedItems;
import cn.com.umpay.frms.preprocessor.cluster.IncrClusterConfiguration;
import com.umpay.loader.cal.RiskPayOrderCalculaterHelper;
import com.umpay.loader.pojo.RiskPayOrder;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.drools.agent.KnowledgeAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisSentinelPool;


import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.bsfit.frms.obj.TaggedID;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.SerializationUtils;
import org.drools.agent.KnowledgeAgent;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;

public class UmpayPreprocessJob
{
  private static final Logger jrtKEkxxdH = LoggerFactory.getLogger(UmpayPreprocessJob.class);
  private IncrPayOrder vjYbNumBBE;
  private KnowledgeAgent bSnahKfDxl;
  private FastKnowledgeAgent meKrvCfWOQ;
  private JedisSentinelPool VzAHWaVZos;
  private boolean beEncABYgg = false;
  private TransformPayOrderToMemcachedItem MsYfPmdGXp;
  private RiskPayOrderCalculaterHelper VCYfbOySbP;
  private int SrZgqlbHZb = 10;
  private int uHFZLTBWck = 100;
  ExecutorService MEjnWwjnJJ;
  ExecutorService muozJDZhHV = null;
  
  public List<RiskPayOrder> preprocess()
  {
    int i = 0;
    if (IncrClusterConfiguration.CLUSTER_SIZE.get() > 1)
    {
      if (IncrClusterConfiguration.CLUSTER_INDEX.get() == 0) {
        i = 1;
      }
    }
    else {
      i = 1;
    }
    if (i != 0)
    {
      jrtKEkxxdH.debug("I'm the master node");
      try
      {
        long l1 = System.currentTimeMillis();
        List localList = this.vjYbNumBBE.getIncrPayOrders();
        if ((localList == null) || (localList.size() == 0)) {
          return null;
        }
        long l2 = System.currentTimeMillis() - l1;
        l1 = System.currentTimeMillis();
        Object localObject = new ArrayList();
        localObject = this.MsYfPmdGXp.transform(localList);
        long l3 = System.currentTimeMillis() - l1;
        l1 = System.currentTimeMillis();
        localObject = MEjnWwjnJJ((List)localObject);
        jrtKEkxxdH.debug("{} MemCachedItems need to be proceeded after duplicated items removed.");
        muozJDZhHV((List)localObject, localList);
        muozJDZhHV(localList);
        MEjnWwjnJJ((List)localObject, localList);
        jrtKEkxxdH.info("{}ms:{}ms,{}ms,{}ms", new Object[] { Long.valueOf(l2 + l3 + System.currentTimeMillis() - l1), Long.valueOf(l2), Long.valueOf(l3), Long.valueOf(System.currentTimeMillis() - l1) });
      }
      catch (Exception localException)
      {
        jrtKEkxxdH.warn(localException.getMessage(), localException);
      }
    }
    else
    {
      jrtKEkxxdH.debug("I'm the slave node, waiting for promotion...");
    }
    return null;
  }
  
  private void muozJDZhHV(List<Object> paramList)
  {
    long l1 = System.currentTimeMillis();
    try
    {
      ArrayList localArrayList = new ArrayList();
      this.VCYfbOySbP.setPayOrders(paramList);
      List localList1 = this.MEjnWwjnJJ.invokeAll(this.VCYfbOySbP.getCalculationTasks());
      Iterator localIterator = localList1.iterator();
      while (localIterator.hasNext())
      {
        Future localFuture = (Future)localIterator.next();
        List localList2 = (List)localFuture.get();
        if (localList2 != null) {
          localArrayList.addAll(localList2);
        }
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      localInterruptedException.printStackTrace();
    }
    catch (ExecutionException localExecutionException)
    {
      localExecutionException.printStackTrace();
    }
    long l2 = System.currentTimeMillis();
    System.err.println(l2 - l1 + "ms");
  }
  
  private void muozJDZhHV(List<MemCachedItem> paramList, List<Object> paramList1)
  {
    long l1 = System.currentTimeMillis();
    SumAccumulateFunction localSumAccumulateFunction = new SumAccumulateFunction();
    Iterator localIterator1 = paramList.iterator();
    MemCachedItem localMemCachedItem;
    while (localIterator1.hasNext())
    {
      localMemCachedItem = (MemCachedItem)localIterator1.next();
      localMemCachedItem.put("a", localSumAccumulateFunction.createContext());
    }
    localIterator1 = paramList.iterator();
    while (localIterator1.hasNext())
    {
      localMemCachedItem = (MemCachedItem)localIterator1.next();
      String str1 = localMemCachedItem.getPrimaryKey();
      String str2 = localMemCachedItem.getPrimaryTag();
      if ((str2 != null) && (str1.equals("MobileMerProduct")))
      {
        Serializable localSerializable = (Serializable)localMemCachedItem.get("a");
        Iterator localIterator2 = paramList1.iterator();
        while (localIterator2.hasNext())
        {
          Object localObject = localIterator2.next();
          RiskPayOrder localRiskPayOrder = (RiskPayOrder)localObject;
          localSumAccumulateFunction.accumulate(localSerializable, new Object[] { localRiskPayOrder.getIntime(), "ph", localRiskPayOrder.getAmount() });
        }
        try
        {
          localMemCachedItem.mergeT("aa", (TimedItems)localSumAccumulateFunction.getResult(localSerializable));
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    }
    long l2 = System.currentTimeMillis();
    System.err.println(l2 - l1 + "ms");
  }
  
  List<MemCachedItem> MEjnWwjnJJ(List<MemCachedItem> paramList)
  {
    if (paramList == null) {
      return new ArrayList();
    }
    HashMap localHashMap = new HashMap();
    localHashMap.clear();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      MemCachedItem localMemCachedItem = (MemCachedItem)localIterator.next();
      localHashMap.put(localMemCachedItem, null);
    }
    return new ArrayList(localHashMap.keySet());
  }
  
  void MEjnWwjnJJ(List<MemCachedItem> paramList, List<Object> paramList1)
  {
    while (paramList.size() > 0)
    {
      ArrayList localArrayList = new ArrayList(this.uHFZLTBWck);
      Object localObject = new ArrayList();
      if (paramList.size() > this.SrZgqlbHZb)
      {
        localObject = paramList.subList(0, this.SrZgqlbHZb);
        paramList = paramList.subList(this.SrZgqlbHZb, paramList.size());
      }
      else
      {
        localObject = paramList;
        paramList = new ArrayList();
      }
      int i = 0;
      if (((List)localObject).size() % this.uHFZLTBWck == 0) {
        i = ((List)localObject).size() / this.uHFZLTBWck;
      } else {
        i = ((List)localObject).size() / this.uHFZLTBWck + 1;
      }
      int j = 0;
      int k = j + i;
      for (int m = 0; m < this.uHFZLTBWck; m++)
      {
        if (k > ((List)localObject).size()) {
          k = ((List)localObject).size();
        }
        UmpayPreprocessJob.LocalProcessTask localLocalProcessTask = new UmpayPreprocessJob.LocalProcessTask(this.meKrvCfWOQ.getKagentPool().get(), this.VzAHWaVZos, ((List)localObject).subList(j, k), paramList1, this.beEncABYgg);
        j = k;
        k = j + i;
        localArrayList.add(localLocalProcessTask);
      }
      try
      {
        this.MEjnWwjnJJ.invokeAll(localArrayList);
      }
      catch (Exception localException)
      {
        jrtKEkxxdH.error("Calculation thread pool interrupted.", localException);
      }
    }
  }
  
  public void setBatchSize(int paramInt)
  {
    this.SrZgqlbHZb = paramInt;
  }
  
  public void shutdownThreadPool()
  {
    if (this.MEjnWwjnJJ != null)
    {
      try
      {
        this.MEjnWwjnJJ.awaitTermination(1L, TimeUnit.SECONDS);
      }
      catch (InterruptedException localInterruptedException)
      {
        jrtKEkxxdH.error("Calculation thread pool interrupted.", localInterruptedException);
      }
      this.MEjnWwjnJJ.shutdown();
    }
  }
  
  public void setThreadSize(int paramInt)
  {
    this.uHFZLTBWck = paramInt;
    this.MEjnWwjnJJ = Executors.newFixedThreadPool(paramInt);
  }
  
  public void setPayOrderScanner(IncrPayOrder paramIncrPayOrder)
  {
    this.vjYbNumBBE = paramIncrPayOrder;
  }
  
  public void setTransformPayOrder(TransformPayOrderToMemcachedItem paramTransformPayOrderToMemcachedItem)
  {
    this.MsYfPmdGXp = paramTransformPayOrderToMemcachedItem;
  }
  
  public void setKagent(KnowledgeAgent paramKnowledgeAgent)
  {
    this.bSnahKfDxl = paramKnowledgeAgent;
  }
  
  public void setPool(JedisSentinelPool paramJedisSentinelPool)
  {
    this.VzAHWaVZos = paramJedisSentinelPool;
  }
  
  public void setKnowledgeLoggerEnable(boolean paramBoolean)
  {
    this.beEncABYgg = paramBoolean;
  }
  
  public void setFastKnowledgeAgent(FastKnowledgeAgent paramFastKnowledgeAgent)
  {
    this.meKrvCfWOQ = paramFastKnowledgeAgent;
  }
  
  public void setCalculaterHelper(RiskPayOrderCalculaterHelper paramRiskPayOrderCalculaterHelper)
  {
    this.VCYfbOySbP = paramRiskPayOrderCalculaterHelper;
  }
  
  //1
  public class LocalProcessTask
  implements Callable<Object>
{
  private final Logger muozJDZhHV = LoggerFactory.getLogger(LocalProcessTask.class);
  private KnowledgeAgent jrtKEkxxdH;
  private JedisSentinelPool vjYbNumBBE;
  private List<MemCachedItem> bSnahKfDxl;
  private List<Object> meKrvCfWOQ;
  private boolean VzAHWaVZos;
  
  public LocalProcessTask(KnowledgeAgent paramKnowledgeAgent, JedisSentinelPool paramJedisSentinelPool, List<MemCachedItem> paramList, List<Object> paramList1, boolean paramBoolean)
  {
    this.bSnahKfDxl = paramList;
    this.meKrvCfWOQ = paramList1;
    this.jrtKEkxxdH = paramKnowledgeAgent;
    this.vjYbNumBBE = paramJedisSentinelPool;
    boolean bool=false;
    this.VzAHWaVZos = bool;
  }
  
  public Object call()
  {
    applyMemCachedItem(this.bSnahKfDxl, this.meKrvCfWOQ);
    return null;
  }
  
  public List<MemCachedItem> get(List<byte[]> paramList)
  {
    Jedis localJedis = null;
    try
    {
      localJedis = this.vjYbNumBBE.getResource();
      ArrayList localArrayList = new ArrayList();
      byte[][] arrayOfByte = new byte[paramList.size()][];
      List localList = localJedis.mget((byte[][])paramList.toArray(arrayOfByte));
      Object localObject1 = localList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        byte[] arrayOfByte1 = (byte[])((Iterator)localObject1).next();
        if ((arrayOfByte1 != null) && (SerializationUtils.deserialize(arrayOfByte1) != null)) {
          localArrayList.add((MemCachedItem)SerializationUtils.deserialize(arrayOfByte1));
        }
      }
      if ((localArrayList != null) && (localArrayList.size() > 0))
      {
        localObject1 = localArrayList;
        return (List<MemCachedItem>) localObject1;
      }
      localObject1 = null;
      return (List<MemCachedItem>) localObject1;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (null != localJedis)
      {
        this.vjYbNumBBE.returnBrokenResource(localJedis);
        localJedis = null;
      }
      if (null != localJedis) {
        this.vjYbNumBBE.returnResource(localJedis);
      }
    }
    finally
    {
      if (null != localJedis) {
        this.vjYbNumBBE.returnResource(localJedis);
      }
    }
    return null;
  }
  
  public List<MemCachedItem> applyMemCachedItem(List<MemCachedItem> paramList, List<Object> paramList1)
  {
    long l1 = System.currentTimeMillis();
    if ((paramList == null) || (paramList.size() == 0)) {
      return null;
    }
    HashMap localHashMap = new HashMap();
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = paramList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      MemCachedItem   localObject2 = (MemCachedItem)((Iterator)localObject1).next();
      localArrayList.add(((MemCachedItem)localObject2).getMemCachedKey().getBytes());
      localHashMap.put(((MemCachedItem)localObject2).getMemCachedKey(), localObject2);
    }
    localObject1 = get(localArrayList);
    if ((localObject1 != null) && (((List)localObject1).size() > 0))
    {
      Iterator  localObject2 = ((List)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        MemCachedItem localMemCachedItem1 = (MemCachedItem)((Iterator)localObject2).next();
        localHashMap.put(localMemCachedItem1.getMemCachedKey(), localMemCachedItem1);
      }
    }
    Object localObject2 = new ArrayList(localHashMap.values());
    long l2 = 0L;
    Date localDate1 = new Date();
    long l3 = 0L;
    long l4 = 0L;
    long l5 = 0L;
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.set(11, 0);
    localGregorianCalendar.set(12, 0);
    localGregorianCalendar.set(13, 0);
    localGregorianCalendar.set(14, 0);
    Date localDate2 = localGregorianCalendar.getTime();
    String str = new SimpleDateFormat("yyyyMMdd").format(localDate2);
    Long localLong = Long.valueOf(System.currentTimeMillis());
    if ((localObject2 != null) && (((List)localObject2).size() > 0))
    {
      Iterator localIterator = ((List)localObject2).iterator();
      while (localIterator.hasNext())
      {
        MemCachedItem localMemCachedItem2 = (MemCachedItem)localIterator.next();
        localMemCachedItem2.put("今天开始时间", localDate2);
        localMemCachedItem2.put("今天日期", str);
        localMemCachedItem2.put("当前时间", localLong);
      }
    }
    try
    {
      localObject2 = (List)onChange(paramList1, (Collection)localObject2);
      l4 = System.currentTimeMillis() - l1;
      l1 = System.currentTimeMillis();
      if (((List)localObject2).size() > 0) {
        storeMemCachedItem((Collection)localObject2);
      }
      l5 = System.currentTimeMillis() - l1;
      l1 = System.currentTimeMillis();
    }
    catch (Throwable localThrowable)
    {
      this.muozJDZhHV.error("Preprocess error", localThrowable);
      return new ArrayList();
    }
    long l6 = System.currentTimeMillis() - l1;
    long l7 = l6 + l5 + l4;
    this.muozJDZhHV.info("[{}ms]latency :  rule matching[{}ms], cache updating[{}ms], db updating[{}ms], size:[payorder--{} : memitem-- {}]", new Object[] { Long.valueOf(l7), Long.valueOf(l4), Long.valueOf(l5), Long.valueOf(l6), Integer.valueOf(paramList1.size()), Integer.valueOf(paramList.size()) });
    if (l2 != 0L) {
      this.muozJDZhHV.debug(String.format("Data activation duration:[%.2f] ms", new Object[] { Double.valueOf(localDate1.getTime() - 1.0D * l3 / l2) }));
    }
    long l8 = System.currentTimeMillis();
    this.muozJDZhHV.debug(String.format("Calc duration:[%d] ms", new Object[] { Long.valueOf(l8 - l1) }));
    return (List<MemCachedItem>) localObject2;
  }
  
  public void storeMemCachedItem(Collection<MemCachedItem> paramCollection)
  {
    Jedis localJedis = null;
    try
    {
      localJedis = this.vjYbNumBBE.getResource();
      Pipeline localPipeline = localJedis.pipelined();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        MemCachedItem localMemCachedItem = (MemCachedItem)localIterator.next();
        try
        {
          byte[] arrayOfByte = localMemCachedItem.getMemCachedKey().getBytes();
          localPipeline.set(arrayOfByte, SerializationUtils.serialize(localMemCachedItem));
          if (localMemCachedItem.get("transDate") != null) {
            localPipeline.expire(arrayOfByte, 864000);
          } else if (localMemCachedItem.get("expire") != null) {
            localPipeline.expire(arrayOfByte, Integer.valueOf(localMemCachedItem.get("expire").toString()).intValue() * 24 * 3600);
          } else {
            localPipeline.expire(arrayOfByte, 7776000);
          }
        }
        catch (Exception localException2)
        {
          this.muozJDZhHV.error("Save memcached error", localException2);
        }
      }
      localPipeline.sync();
      if (null != localJedis) {
        this.vjYbNumBBE.returnResource(localJedis);
      }
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      if (null != localJedis)
      {
        this.vjYbNumBBE.returnBrokenResource(localJedis);
        localJedis = null;
      }
      if (null != localJedis) {
        this.vjYbNumBBE.returnResource(localJedis);
      }
    }
    finally
    {
      if (null != localJedis) {
        this.vjYbNumBBE.returnResource(localJedis);
      }
    }
  }
  
  public List<MemCachedItem> applyChangeSet(List<TaggedID> paramList)
  {
    return null;
  }
  
  public Collection<MemCachedItem> onChange(List<Object> paramList, Collection<MemCachedItem> paramCollection)
  {
    StatelessKnowledgeSession localStatelessKnowledgeSession = this.jrtKEkxxdH.newStatelessKnowledgeSession();
    KnowledgeRuntimeLogger localKnowledgeRuntimeLogger = null;
    if (this.VzAHWaVZos)
    {
      this.muozJDZhHV.info("-------------------------Drools log start-------------------------");
      localKnowledgeRuntimeLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(localStatelessKnowledgeSession);
    }
    List localList = MEjnWwjnJJ(paramList, paramCollection);
    ExecutionResults localExecutionResults = null;
    localExecutionResults = (ExecutionResults)localStatelessKnowledgeSession.execute(CommandFactory.newBatchExecution(localList));
    if (this.VzAHWaVZos)
    {
      localKnowledgeRuntimeLogger.close();
      this.muozJDZhHV.info("-------------------------Drools log end---------------------------");
    }
    QueryResults localQueryResults = (QueryResults)localExecutionResults.getValue("item");
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localQueryResults.iterator();
    while (localIterator.hasNext())
    {
      QueryResultsRow localQueryResultsRow = (QueryResultsRow)localIterator.next();
      if ((localQueryResultsRow != null) && (localQueryResultsRow.get("item") != null)) {
        localArrayList.add((MemCachedItem)localQueryResultsRow.get("item"));
      }
    }
    return localArrayList;
  }
  
  private List<Command<?>> MEjnWwjnJJ(List<Object> paramList, Collection<MemCachedItem> paramCollection)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramCollection != null) && (paramCollection.size() > 0)) {
      localArrayList.add(CommandFactory.newInsertElements(paramCollection));
    }
    if ((paramList != null) && (paramList.size() > 0)) {
      localArrayList.add(CommandFactory.newInsertElements(paramList));
    }
    localArrayList.add(CommandFactory.newFireAllRules());
    localArrayList.add(CommandFactory.newQuery("item", "query memcached item"));
    return localArrayList;
  }
}

}
