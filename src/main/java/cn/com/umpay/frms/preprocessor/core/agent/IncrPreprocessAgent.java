package cn.com.umpay.frms.preprocessor.core.agent;

import cn.com.bsfit.frms.base.StatisticService;
import cn.com.bsfit.frms.base.store.CachedItemStorer;
import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.bsfit.frms.obj.NodeStat;
import cn.com.bsfit.frms.obj.TaggedID;
import cn.com.umpay.frms.preprocessor.core.ChangeSetAgent;
import cn.com.umpay.frms.preprocessor.core.ChangeSetHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncrPreprocessAgent
  implements ChangeSetAgent
{
  private static final Logger MEjnWwjnJJ = LoggerFactory.getLogger(IncrPreprocessAgent.class);
  private CachedItemStorer muozJDZhHV;
  private ChangeSetHandler jrtKEkxxdH;
  
  public void setHandler(ChangeSetHandler paramChangeSetHandler)
  {
    this.jrtKEkxxdH = paramChangeSetHandler;
  }
  
  public void setStorer(CachedItemStorer paramCachedItemStorer)
  {
    this.muozJDZhHV = paramCachedItemStorer;
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
      MemCachedItem  localObject2 = (MemCachedItem)((Iterator)localObject1).next();
      localArrayList.add(((MemCachedItem)localObject2).getMemCachedKey().getBytes());
      localHashMap.put(((MemCachedItem)localObject2).getMemCachedKey(), localObject2);
    }
    localObject1 = this.muozJDZhHV.get(localArrayList);
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
      localObject2 = (List)this.jrtKEkxxdH.onChange(paramList1, (Collection)localObject2);
      l4 = System.currentTimeMillis() - l1;
      l1 = System.currentTimeMillis();
      if (((List)localObject2).size() > 0)
      {
        MEjnWwjnJJ.info("-------------------------Redis saving start-------------------");
        this.muozJDZhHV.storeMemCachedItem((Collection)localObject2);
        MEjnWwjnJJ.info("-------------------------Redis saving end---------------------");
      }
      l5 = System.currentTimeMillis() - l1;
      l1 = System.currentTimeMillis();
    }
    catch (Throwable localThrowable)
    {
      MEjnWwjnJJ.error("Preprocess error", localThrowable);
      return new ArrayList();
    }
    long l6 = System.currentTimeMillis() - l1;
    long l7 = l6 + l5 + l4;
    StatisticService.stat.succReq(l7, paramList.size());
    MEjnWwjnJJ.info("[{}ms]latency :  rule matching[{}ms], cache updating[{}ms], db updating[{}ms], size:[payorder--{} : memitem-- {}]", new Object[] { Long.valueOf(l7), Long.valueOf(l4), Long.valueOf(l5), Long.valueOf(l6), Integer.valueOf(paramList1.size()), Integer.valueOf(paramList.size()) });
    if (l2 != 0L) {
      MEjnWwjnJJ.debug(String.format("Data activation duration:[%.2f] ms", new Object[] { Double.valueOf(localDate1.getTime() - 1.0D * l3 / l2) }));
    }
    long l8 = System.currentTimeMillis();
    MEjnWwjnJJ.debug(String.format("Calc duration:[%d] ms", new Object[] { Long.valueOf(l8 - l1) }));
    return (List<MemCachedItem>) localObject2;
  }
  
  public List<MemCachedItem> applyChangeSet(List<TaggedID> paramList)
  {
    return null;
  }
}
