package cn.com.umpay.drools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import org.drools.ChangeSet;
import org.drools.SystemEventListener;
import org.drools.SystemEventListenerFactory;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.concurrent.ExecutorProvider;
import org.drools.concurrent.ExecutorProviderFactory;
import org.drools.event.io.ResourceChangeListener;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ResourceChangeNotifierImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgeDefinition;
import org.drools.io.Resource;
import org.drools.io.internal.InternalResource;

import java.util.concurrent.LinkedBlockingQueue;
import org.drools.ChangeSet;
import org.drools.SystemEventListener;

public class FastKnowledgeAgent
  implements ResourceChangeListener
{
  private static final Logger MEjnWwjnJJ = LoggerFactory.getLogger(FastKnowledgeAgent.class);
  private final String muozJDZhHV;
  private SystemEventListener jrtKEkxxdH;
  private final LinkedBlockingQueue<ChangeSet> vjYbNumBBE;
  private ResourceChangeNotifierImpl bSnahKfDxl;
  private final FastKnowledgeAgent.RegisteredResourceMap meKrvCfWOQ = new FastKnowledgeAgent.RegisteredResourceMap();
  private FastKnowledgeAgent.ChangeSetNotificationDetector VzAHWaVZos;
  private Future<Object> beEncABYgg;
  private KnowledgeAgentPool MsYfPmdGXp;
  
  public FastKnowledgeAgent(String paramString, KnowledgeAgentConfiguration paramKnowledgeAgentConfiguration)
  {
    this.muozJDZhHV = paramString;
    this.jrtKEkxxdH = SystemEventListenerFactory.getSystemEventListener();
    this.vjYbNumBBE = new LinkedBlockingQueue();
    boolean bool = false;
    if (paramKnowledgeAgentConfiguration != null)
    {
      this.bSnahKfDxl = ((ResourceChangeNotifierImpl)ResourceFactory.getResourceChangeNotifierService());
      if (paramKnowledgeAgentConfiguration.isMonitorChangeSetEvents()) {
        bool = true;
      }
    }
    monitorResourceChangeEvents(bool);
  }
  
  public void resourcesChanged(ChangeSet paramChangeSet)
  {
    try
    {
      this.jrtKEkxxdH.debug("KnowledgeAgent received ChangeSet changed notification");
      this.vjYbNumBBE.put(paramChangeSet);
    }
    catch (InterruptedException localInterruptedException)
    {
      this.jrtKEkxxdH.exception(new RuntimeException("KnowledgeAgent error while adding ChangeSet notification to queue", localInterruptedException));
    }
  }
  
  public void monitorResourceChangeEvents(boolean paramBoolean)
  {
    HashSet localHashSet = new HashSet();
    localHashSet.addAll(this.meKrvCfWOQ.getAllResources());
    Iterator localIterator = localHashSet.iterator();
    while (localIterator.hasNext())
    {
      Resource localResource = (Resource)localIterator.next();
      if (paramBoolean)
      {
        this.jrtKEkxxdH.debug("KnowledgeAgent subscribing from resource=" + localResource);
        this.bSnahKfDxl.subscribeResourceChangeListener(this, localResource);
      }
      else
      {
        this.jrtKEkxxdH.debug("KnowledgeAgent unsubscribing from resource=" + localResource);
        this.bSnahKfDxl.unsubscribeResourceChangeListener(this, localResource);
      }
    }
    if ((!paramBoolean) && (this.VzAHWaVZos != null))
    {
      this.VzAHWaVZos.stop();
      this.beEncABYgg.cancel(true);
      this.VzAHWaVZos = null;
    }
    else if ((paramBoolean) && (this.VzAHWaVZos == null))
    {
      this.VzAHWaVZos = new FastKnowledgeAgent.ChangeSetNotificationDetector(this, this.vjYbNumBBE, this.jrtKEkxxdH);

      this.beEncABYgg = ExecutorProviderFactory.getExecutorProvider().getCompletionService().submit(this.VzAHWaVZos, Boolean.valueOf(true));
    }
  }
  
  public void applyChangeSet(ChangeSet paramChangeSet)
  {
    MEjnWwjnJJ.info("Applied changeset: {}", paramChangeSet);
    if (this.MsYfPmdGXp != null) {
      this.MsYfPmdGXp.applyChangeSet(paramChangeSet);
    }
  }
  
  public KnowledgeAgentPool getKagentPool()
  {
    return this.MsYfPmdGXp;
  }
  
  public void setKagentPool(KnowledgeAgentPool paramKnowledgeAgentPool)
  {
    this.MsYfPmdGXp = paramKnowledgeAgentPool;
  }
  
  
  //类1
  class RegisteredResourceMap
  {
    private final Map<Resource, Set<KnowledgeDefinition>> MEjnWwjnJJ = new HashMap();
    
    public boolean createNewResourceEntry(Resource paramResource)
    {
      if (!isResourceMapped(paramResource))
      {
        this.MEjnWwjnJJ.put(paramResource, new HashSet());
        return true;
      }
      return false;
    }
    
    public boolean putDefinition(Resource paramResource, KnowledgeDefinition paramKnowledgeDefinition)
    {
      Object localObject = (Set)this.MEjnWwjnJJ.get(paramResource);
      if (localObject == null)
      {
        localObject = new HashSet();
        this.MEjnWwjnJJ.put(paramResource, (Set<KnowledgeDefinition>) localObject);
      }
      return (paramKnowledgeDefinition != null) && (((Set)localObject).add(paramKnowledgeDefinition));
    }
    
    public Set<KnowledgeDefinition> removeDefinitions(Resource paramResource)
    {
      return (Set)this.MEjnWwjnJJ.remove(paramResource);
    }
    
    public Set<KnowledgeDefinition> getDefinitions(Resource paramResource)
    {
      return getDefinitions(paramResource, false);
    }
    
    public Set<KnowledgeDefinition> getDefinitions(Resource paramResource, boolean paramBoolean)
    {
      Object localObject = (Set)this.MEjnWwjnJJ.get(paramResource);
      if ((paramBoolean) && (localObject == null)) {
        localObject = new HashSet();
      }
      return (Set<KnowledgeDefinition>) localObject;
    }
    
    public boolean isResourceMapped(Resource paramResource)
    {
      return this.MEjnWwjnJJ.containsKey(paramResource);
    }
    
    public Set<Resource> getAllResources()
    {
      return this.MEjnWwjnJJ.keySet();
    }
    
    public boolean onlyHasPKGResources()
    {
      Iterator localIterator = this.MEjnWwjnJJ.keySet().iterator();
      while (localIterator.hasNext())
      {
        Resource localResource = (Resource)localIterator.next();
        if (((InternalResource)localResource).getResourceType() != ResourceType.PKG) {
          return false;
        }
      }
      return true;
    }
  }
  
  //类2
  public class ChangeSetNotificationDetector
  implements Runnable
{
  private final LinkedBlockingQueue<ChangeSet> MEjnWwjnJJ;
  private volatile boolean muozJDZhHV;
  private final FastKnowledgeAgent jrtKEkxxdH;
  private final SystemEventListener vjYbNumBBE;
  
  public ChangeSetNotificationDetector(FastKnowledgeAgent paramFastKnowledgeAgent, LinkedBlockingQueue<ChangeSet> paramLinkedBlockingQueue, SystemEventListener paramSystemEventListener)
  {
    this.MEjnWwjnJJ = paramLinkedBlockingQueue;
    this.jrtKEkxxdH = paramFastKnowledgeAgent;
    this.vjYbNumBBE = paramSystemEventListener;
    this.muozJDZhHV = true;
  }
  
  public void stop()
  {
    this.muozJDZhHV = false;
  }
  
  public void run()
  {
    if (this.muozJDZhHV) {
      this.vjYbNumBBE.info("KnowledgeAgent has started listening for ChangeSet notifications");
    }
    while (this.muozJDZhHV)
    {
      Object localObject = null;
      try
      {
        this.jrtKEkxxdH.applyChangeSet((ChangeSet)this.MEjnWwjnJJ.take());
      }
      catch (InterruptedException localInterruptedException)
      {
        localObject = localInterruptedException;
      }
      Thread.yield();
      if ((this.muozJDZhHV) && (localObject != null)) {
        this.vjYbNumBBE.exception(new RuntimeException("KnowledgeAgent ChangeSet notification thread has been interrupted, but shutdown was not scheduled", (Throwable) localObject));
      }
    }
    this.vjYbNumBBE.info("KnowledgeAgent has stopped listening for ChangeSet notifications");
  }
}
}
