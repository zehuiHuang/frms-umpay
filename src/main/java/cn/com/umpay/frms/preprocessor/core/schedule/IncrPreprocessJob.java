package cn.com.umpay.frms.preprocessor.core.schedule;

import cn.com.bsfit.frms.base.load.IncrPayOrder;
import cn.com.bsfit.frms.base.load.TransformPayOrderToMemcachedItem;
import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.umpay.frms.preprocessor.cluster.IncrClusterConfiguration;
import cn.com.umpay.frms.preprocessor.core.ChangeSetAgent;
import com.umpay.loader.pojo.RiskPayOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncrPreprocessJob
{
  private static final Logger jrtKEkxxdH = LoggerFactory.getLogger(IncrPreprocessJob.class);
  private IncrPayOrder vjYbNumBBE;
  private ChangeSetAgent bSnahKfDxl;
  private TransformPayOrderToMemcachedItem meKrvCfWOQ;
  private int VzAHWaVZos = 10;
  private int beEncABYgg = 100;
  ExecutorService MEjnWwjnJJ = Executors.newFixedThreadPool(this.beEncABYgg);
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
        localObject = this.meKrvCfWOQ.transform(localList);
        long l3 = System.currentTimeMillis() - l1;
        l1 = System.currentTimeMillis();
        localObject = MEjnWwjnJJ((List)localObject);
        jrtKEkxxdH.debug("{} MemCachedItems need to be proceeded after duplicated items removed.");
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
      ArrayList localArrayList = new ArrayList(this.beEncABYgg);
      Object localObject = new ArrayList();
      if (paramList.size() > this.VzAHWaVZos)
      {
        localObject = paramList.subList(0, this.VzAHWaVZos);
        paramList = paramList.subList(this.VzAHWaVZos, paramList.size());
      }
      else
      {
        localObject = paramList;
        paramList = new ArrayList();
      }
      int i = 0;
      if (((List)localObject).size() % this.beEncABYgg == 0) {
        i = ((List)localObject).size() / this.beEncABYgg;
      } else {
        i = ((List)localObject).size() / this.beEncABYgg + 1;
      }
      int j = 0;
      int k = j + i;
      for (int m = 0; m < this.beEncABYgg; m++)
      {
        if (k > ((List)localObject).size()) {
          k = ((List)localObject).size();
        }
        IncrPreprocessJob.LocalProcessTask localLocalProcessTask = new IncrPreprocessJob.LocalProcessTask(((List)localObject).subList(j, k), paramList1);
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
    this.VzAHWaVZos = paramInt;
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
    this.beEncABYgg = paramInt;
    if (this.beEncABYgg != paramInt)
    {
      shutdownThreadPool();
      this.MEjnWwjnJJ = Executors.newFixedThreadPool(paramInt);
    }
  }
  
  public void setPayOrderScanner(IncrPayOrder paramIncrPayOrder)
  {
    this.vjYbNumBBE = paramIncrPayOrder;
  }
  
  public void setIncrPreprocessAgent(ChangeSetAgent paramChangeSetAgent)
  {
    this.bSnahKfDxl = paramChangeSetAgent;
  }
  
  public void setTransformPayOrder(TransformPayOrderToMemcachedItem paramTransformPayOrderToMemcachedItem)
  {
    this.meKrvCfWOQ = paramTransformPayOrderToMemcachedItem;
  }
  
  //1

public class LocalProcessTask
  implements Callable<Object>
{
  private List<MemCachedItem> muozJDZhHV;
  private List<Object> jrtKEkxxdH;
  
  public LocalProcessTask(List<MemCachedItem> paramList, List<Object> paramList1)
  {
    this.muozJDZhHV = paramList;
    Object localObject;
    this.jrtKEkxxdH =  paramList1;
  }
  
  public Object call()
  {
    bSnahKfDxl.applyMemCachedItem(this.muozJDZhHV, this.jrtKEkxxdH);
    return null;
  }
}

}
