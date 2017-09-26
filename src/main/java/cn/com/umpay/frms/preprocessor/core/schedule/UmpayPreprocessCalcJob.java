package cn.com.umpay.frms.preprocessor.core.schedule;

import cn.com.bsfit.frms.base.load.IncrPayOrder;
import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.umpay.frms.preprocessor.cluster.IncrClusterConfiguration;
import com.umpay.loader.cal.RiskPayOrderCalculaterHelper;
import com.umpay.loader.pojo.RiskPayOrder;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisSentinelPool;

public class UmpayPreprocessCalcJob
{
  private static final Logger muozJDZhHV = LoggerFactory.getLogger(UmpayPreprocessCalcJob.class);
  private static final Logger jrtKEkxxdH = LoggerFactory.getLogger("MpspLog");
  private IncrPayOrder vjYbNumBBE;
  private JedisSentinelPool bSnahKfDxl;
  private RiskPayOrderCalculaterHelper meKrvCfWOQ;
  private int VzAHWaVZos = 100;
  ExecutorService MEjnWwjnJJ;
  
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
      muozJDZhHV.debug("I'm the master node");
      try
      {
        long l1 = System.currentTimeMillis();
        List localList = this.vjYbNumBBE.getIncrPayOrders();
        if ((localList == null) || (localList.size() == 0)) {
          return null;
        }
        long l2 = System.currentTimeMillis() - l1;
        l1 = System.currentTimeMillis();
        Collections.sort(localList, new UmpayPreprocessCalcJob1(null));
        long l3 = System.currentTimeMillis() - l1;
        l1 = System.currentTimeMillis();
        Object localObject1 = localList.get(localList.size() - 1);
        int j = 1;
        Object localObject2;
        if ((localObject1 instanceof RiskPayOrder))
        {
          localObject2 = (RiskPayOrder)localObject1;
          if (((RiskPayOrder)localObject2).getIntime().getTime() < l1 - 5184000000L) {
            j = 0;
          }
        }
        if (j != 0) {
          localObject2 = MEjnWwjnJJ(localList);
        }
        long l4 = System.currentTimeMillis() - l1;
        l1 = System.currentTimeMillis();
        jrtKEkxxdH.info("total:{}ms,db:{}ms,sort:{}ms,calc:{}ms,size:{}", new Object[] { Long.valueOf(l2 + l3 + l4), Long.valueOf(l2), Long.valueOf(l3), Long.valueOf(l4), Integer.valueOf(localList.size()) });
      }
      catch (Exception localException)
      {
        jrtKEkxxdH.warn(localException.getMessage(), localException);
      }
    }
    else
    {
      muozJDZhHV.debug("I'm the slave node, waiting for promotion...");
    }
    return null;
  }
  
  private List<MemCachedItem> MEjnWwjnJJ(List<Object> paramList)
  {
    long l1 = System.currentTimeMillis();
    ArrayList localArrayList = new ArrayList();
    try
    {
      this.meKrvCfWOQ.setPayOrders(paramList);
      List localList1 = this.MEjnWwjnJJ.invokeAll(this.meKrvCfWOQ.getCalculationTasks());
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
    return localArrayList;
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
    this.VzAHWaVZos = paramInt;
    this.MEjnWwjnJJ = Executors.newFixedThreadPool(paramInt);
  }
  
  public void setPayOrderScanner(IncrPayOrder paramIncrPayOrder)
  {
    this.vjYbNumBBE = paramIncrPayOrder;
  }
  
  public void setPool(JedisSentinelPool paramJedisSentinelPool)
  {
    this.bSnahKfDxl = paramJedisSentinelPool;
  }
  
  public void setCalculaterHelper(RiskPayOrderCalculaterHelper paramRiskPayOrderCalculaterHelper)
  {
    this.meKrvCfWOQ = paramRiskPayOrderCalculaterHelper;
  }
  
//  public static void main(String[] paramArrayOfString)
//  {
//    ArrayList<Object> localArrayList = new ArrayList<Object>();
//    RiskPayOrder localRiskPayOrder1 = new RiskPayOrder();
//    localRiskPayOrder1.setIntime(new Date());
//    localArrayList.add(localRiskPayOrder1);
//    RiskPayOrder localRiskPayOrder2 = new RiskPayOrder();
//    localRiskPayOrder2.setIntime(new Date(System.currentTimeMillis() - 10000L));
//    localArrayList.add(localRiskPayOrder2);
//    RiskPayOrder localRiskPayOrder3 = new RiskPayOrder();
//    localRiskPayOrder3.setIntime(new Date(System.currentTimeMillis() + 10000L));
//    localArrayList.add(localRiskPayOrder3);
//    Collections.sort(localArrayList, new UmpayPreprocessCalcJob2(null));
//    //Collections.sort(localArrayList, new UmpayPreprocessCalcJob1(null));
//    Iterator localIterator = localArrayList.iterator();
//    while (localIterator.hasNext())
//    {
//      Object localObject = localIterator.next();
//      System.out.println(((RiskPayOrder)localObject).getIntime());
//    }
//  }
  
  //1
  class UmpayPreprocessCalcJob1
  implements Comparator<Object>
{
  UmpayPreprocessCalcJob1(UmpayPreprocessCalcJob paramUmpayPreprocessCalcJob) {}
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    RiskPayOrder localRiskPayOrder1 = (RiskPayOrder)paramObject1;
    RiskPayOrder localRiskPayOrder2 = (RiskPayOrder)paramObject2;
    Long localLong = Long.valueOf(localRiskPayOrder1.getIntime().getTime() - localRiskPayOrder2.getIntime().getTime());
    return localLong.intValue();
  }
}
//2
//  final class UmpayPreprocessCalcJob2
//  implements Comparator<Object>
//{
//  UmpayPreprocessCalcJob2(UmpayPreprocessCalcJob paramUmpayPreprocessCalcJob) {}
//  public int compare(Object paramObject1, Object paramObject2)
//  {
//    RiskPayOrder localRiskPayOrder1 = (RiskPayOrder)paramObject1;
//    RiskPayOrder localRiskPayOrder2 = (RiskPayOrder)paramObject2;
//    Long localLong = Long.valueOf(localRiskPayOrder1.getIntime().getTime() - localRiskPayOrder2.getIntime().getTime());
//    return localLong.intValue();
//  }
//}
final  class UmpayPreprocessCalcJob2
  implements Comparator<Object>
{
  UmpayPreprocessCalcJob2(UmpayPreprocessCalcJob paramUmpayPreprocessCalcJob) {}
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    RiskPayOrder localRiskPayOrder1 = (RiskPayOrder)paramObject1;
    RiskPayOrder localRiskPayOrder2 = (RiskPayOrder)paramObject2;
    Long localLong = Long.valueOf(localRiskPayOrder1.getIntime().getTime() - localRiskPayOrder2.getIntime().getTime());
    return localLong.intValue();
  }
}
}
