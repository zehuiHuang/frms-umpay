package cn.com.umpay.frms.preprocessor.core.handler;

import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.umpay.frms.preprocessor.core.ChangeSetHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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

public class KnowledgeSessionHandler
  implements ChangeSetHandler
{
  public static final Logger logger = LoggerFactory.getLogger(KnowledgeSessionHandler.class);
  private boolean MEjnWwjnJJ = true;
  private KnowledgeRuntimeLogger muozJDZhHV;
  private KnowledgeAgent jrtKEkxxdH;
  
  public void setKnowledgeLoggerEnable(boolean paramBoolean)
  {
    this.MEjnWwjnJJ = paramBoolean;
  }
  
  public Collection<MemCachedItem> onChange(List<Object> paramList, Collection<MemCachedItem> paramCollection)
  {
    StatelessKnowledgeSession localStatelessKnowledgeSession = this.jrtKEkxxdH.newStatelessKnowledgeSession();
    if (this.MEjnWwjnJJ)
    {
      logger.info("-------------------------Drools log start-------------------------");
      this.muozJDZhHV = KnowledgeRuntimeLoggerFactory.newConsoleLogger(localStatelessKnowledgeSession);
    }
    List localList = MEjnWwjnJJ(paramList, paramCollection);
    ExecutionResults localExecutionResults = null;
    localExecutionResults = (ExecutionResults)localStatelessKnowledgeSession.execute(CommandFactory.newBatchExecution(localList));
    if (this.MEjnWwjnJJ)
    {
      this.muozJDZhHV.close();
      logger.info("-------------------------Drools log end---------------------------");
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
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    localArrayList2.addAll(paramList);
    localArrayList2.addAll(paramCollection);
    if (localArrayList2.size() > 0) {
      localArrayList1.add(CommandFactory.newInsertElements(localArrayList2));
    }
    localArrayList1.add(CommandFactory.newFireAllRules());
    localArrayList1.add(CommandFactory.newQuery("item", "query memcached item"));
    return localArrayList1;
  }
  
  public void setKagent(KnowledgeAgent paramKnowledgeAgent)
  {
    this.jrtKEkxxdH = paramKnowledgeAgent;
  }
}
