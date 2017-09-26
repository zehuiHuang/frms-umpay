package cn.com.umpay.frms.preprocessor.res;

import cn.com.bsfit.frms.obj.MemCachedItem;

import com.alibaba.fastjson.JSON;
import com.umpay.loader.cal.RiskPayOrderCalculaterHelper;
import com.umpay.loader.pojo.RiskPayOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/rs"})
public class UmpayPreServerResource
{
  private static final Logger mpspLog = LoggerFactory.getLogger("MpspLog");
  @Autowired
  private RiskPayOrderCalculaterHelper calculaterHelper;
  //private static  int threadSize=50;
  @Autowired
  private ExecutorService executorService;
  
 @SuppressWarnings("unchecked")
@RequestMapping(value={"/umpayaudits"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String preprocess(@RequestBody String paramObject)
  {
	 System.out.println("----------------------------------------");
    try
    {
      List localList = JSON.parseArray(paramObject, RiskPayOrder.class);
      long l1 = System.currentTimeMillis();
      if ((localList == null) || (localList.size() == 0)) {
        return null;
      }
      l1 = System.currentTimeMillis();
      Collections.sort(localList, new Comparator<Object>() {
    	@Override
    	public int compare(Object o1, Object o2) {
    		  RiskPayOrder localRiskPayOrder1 = (RiskPayOrder)o1;
    		  RiskPayOrder localRiskPayOrder2 = (RiskPayOrder)o2;
    		  Long localLong = Long.valueOf(localRiskPayOrder1.getIntime().getTime() - localRiskPayOrder2.getIntime().getTime());
    		  return localLong.intValue();
    	}
	});
      long l2 = System.currentTimeMillis() - l1;
      l1 = System.currentTimeMillis();
      Object localObject = localList.get(localList.size() - 1);
      int i = 1;
      if ((localObject instanceof RiskPayOrder))
      {
        RiskPayOrder localRiskPayOrder = (RiskPayOrder)localObject;
        if (localRiskPayOrder.getIntime().getTime() < l1 - 5184000000L) {
          i = 0;
        }
      }
      if (i != 0) {
    	  invokeAllCalculationTasks(localList);
      }
      long l3 = System.currentTimeMillis() - l1;
      l1 = System.currentTimeMillis();
      mpspLog.info("total:{}ms,sort:{}ms,calc:{}ms,size:{}", new Object[] { Long.valueOf(l2 + l3), Long.valueOf(l2), Long.valueOf(l3), Integer.valueOf(localList.size()) });
    }
    catch (Exception localException)
    {
    	mpspLog.error(localException.getMessage(), localException);
      //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"status\":\"error\",\"message\":\"" + localException.getLocalizedMessage() + "\"}").build();
     return "{\"status\":\"error\",\"message\":\"" + localException.getLocalizedMessage() + "\"}";
    }
    //return Response.status(Response.Status.OK).entity("{\"status:ok\"}").build();
    return "{\"status:ok\"}";
  }
  
  private List<MemCachedItem> invokeAllCalculationTasks(List<RiskPayOrder> paramList)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList(paramList.size());
    try
    {
      Object localObject1 = paramList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
    	RiskPayOrder  localObject2 = (RiskPayOrder)((Iterator)localObject1).next();
        localArrayList2.add(localObject2);
      }
      this.calculaterHelper.setPayOrders(localArrayList2);
      List calculationTasks=this.calculaterHelper.getCalculationTasks();
      localObject1 = this.executorService.invokeAll(calculationTasks);
      Object localObject2 = ((List)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Future localFuture = (Future)((Iterator)localObject2).next();
        List localList = (List)localFuture.get();
        if (localList != null) {
          localArrayList1.addAll(localList);
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
    return localArrayList1;
  }
  
//  public void setThreadSize(int paramInt)
//  {
//    this.jrtKEkxxdH = paramInt;
//    this.vjYbNumBBE = Executors.newFixedThreadPool(this.jrtKEkxxdH);
//  }
  
  public RiskPayOrderCalculaterHelper getCalculaterHelper()
  {
    return this.calculaterHelper;
  }
  
  public void setCalculaterHelper(RiskPayOrderCalculaterHelper paramRiskPayOrderCalculaterHelper)
  {
    this.calculaterHelper = paramRiskPayOrderCalculaterHelper;
  }
  
//1
//  class UmpayPreServerResource1
//  implements Comparator<Object>
//{
//  UmpayPreServerResource1(UmpayPreServerResource paramUmpayPreServerResource) {}
//  
//  public int compare(Object paramObject1, Object paramObject2)
//  {
//    RiskPayOrder localRiskPayOrder1 = (RiskPayOrder)paramObject1;
//    RiskPayOrder localRiskPayOrder2 = (RiskPayOrder)paramObject2;
//    Long localLong = Long.valueOf(localRiskPayOrder1.getIntime().getTime() - localRiskPayOrder2.getIntime().getTime());
//    return localLong.intValue();
//  }
//}
  
}
