package cn.com.umpay.drools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import org.drools.ChangeSet;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.SystemEventListener;
import org.drools.SystemEventListenerFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgent.ResourceStatus;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.ResourceDiffProducer;
import org.drools.agent.impl.BinaryResourceDiffProducerImpl;
import org.drools.agent.impl.KnowledgeAgentConfigurationImpl;
import org.drools.agent.impl.KnowledgeAgentImpl;
import org.drools.agent.impl.KnowledgeAgentImpl.ChangeSetNotificationDetector;
import org.drools.agent.impl.ResourceDiffResult;
import org.drools.base.ClassFieldAccessorStore;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.common.AbstractRuleBase;
import org.drools.common.InternalRuleBase;
import org.drools.core.util.DroolsStreamUtils;
import org.drools.definition.KnowledgeDefinition;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.process.Process;
import org.drools.definitions.impl.KnowledgePackageImp;
import org.drools.event.KnowledgeAgentEventSupport;
import org.drools.event.knowledgeagent.KnowledgeAgentEventListener;
import org.drools.impl.InternalKnowledgeBase;
import org.drools.impl.KnowledgeBaseImpl;
import org.drools.impl.StatelessKnowledgeSessionImpl;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.io.ResourcedObject;
import org.drools.io.impl.ClassPathResource;
import org.drools.io.impl.ReaderResource;
import org.drools.io.internal.InternalResource;
import org.drools.reteoo.ReteooBuilder;
import org.drools.rule.DialectRuntimeRegistry;
import org.drools.rule.Function;
import org.drools.rule.JavaDialectRuntimeData;
import org.drools.rule.JavaDialectRuntimeData.TypeDeclarationClassLoader;
import org.drools.rule.Package;
import org.drools.rule.Query;
import org.drools.rule.Rule;
import org.drools.rule.TypeDeclaration;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.util.CompositeClassLoader;
import org.drools.xml.ChangeSetSemanticModule;
import org.drools.xml.SemanticModules;
import org.drools.xml.XmlChangeSetReader;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgeDefinition;
import org.drools.io.Resource;
import org.drools.io.internal.InternalResource;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.drools.definition.KnowledgeDefinition;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;

public class SingleThreadKnowledgeAgent
  implements KnowledgeAgent
{
  private final String MEjnWwjnJJ;
  private final Set<Resource> muozJDZhHV;
  private KnowledgeBase jrtKEkxxdH;
  private boolean vjYbNumBBE;
  private SystemEventListener bSnahKfDxl;
  private boolean meKrvCfWOQ;
  private boolean VzAHWaVZos;
  private final LinkedBlockingQueue<ChangeSet> beEncABYgg;
  private Future<Boolean> MsYfPmdGXp;
  private KnowledgeAgentImpl.ChangeSetNotificationDetector VCYfbOySbP;
  private SemanticModules SrZgqlbHZb;
  private final SingleThreadKnowledgeAgent.RegisteredResourceMap uHFZLTBWck = new SingleThreadKnowledgeAgent.RegisteredResourceMap();
  private final Map<Resource, String> CRxGNsliSY = new HashMap();
  private final KnowledgeAgentEventSupport GbWmmGEEYt = new KnowledgeAgentEventSupport();
  private final KnowledgeBuilderConfiguration iJgXNHlULI;
  private int tkFQOWXdXf = 0;
  
  public SingleThreadKnowledgeAgent(String paramString, KnowledgeBase paramKnowledgeBase, KnowledgeAgentConfiguration paramKnowledgeAgentConfiguration, KnowledgeBuilderConfiguration paramKnowledgeBuilderConfiguration)
  {
    this.MEjnWwjnJJ = paramString;
    this.jrtKEkxxdH = paramKnowledgeBase;
    this.iJgXNHlULI = paramKnowledgeBuilderConfiguration;
    this.muozJDZhHV = new HashSet();
    this.bSnahKfDxl = SystemEventListenerFactory.getSystemEventListener();
    this.beEncABYgg = new LinkedBlockingQueue();
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramKnowledgeAgentConfiguration != null)
    {
      this.vjYbNumBBE = ((KnowledgeAgentConfigurationImpl)paramKnowledgeAgentConfiguration).isNewInstance();
      this.VzAHWaVZos = paramKnowledgeAgentConfiguration.isUseKBaseClassLoaderForCompiling();
      if (paramKnowledgeAgentConfiguration.isScanDirectories()) {
        this.meKrvCfWOQ = true;
      }
      this.tkFQOWXdXf = paramKnowledgeAgentConfiguration.getValidationTimeout();
    }
    autoBuildResourceMapping();
    this.bSnahKfDxl.info("KnowledgeAgent created, with configuration:\nmonitorChangeSetEvents=" + bool2 + " scanResources=" + bool1 + " scanDirectories=" + this.meKrvCfWOQ + " newInstance=" + this.vjYbNumBBE);
  }
  
  public void setSystemEventListener(SystemEventListener paramSystemEventListener)
  {
    this.bSnahKfDxl = paramSystemEventListener;
  }
  
  public Set<Resource> getResourceDirectories()
  {
    return this.muozJDZhHV;
  }
  
  public boolean isNewInstance()
  {
    return this.vjYbNumBBE;
  }
  
  public boolean isUseKBaseClassLoaderForCompiling()
  {
    return this.VzAHWaVZos;
  }
  
  public void applyChangeSet(Resource paramResource)
  {
    ChangeSet localChangeSet = getChangeSet(paramResource);
    if (localChangeSet != null) {
      applyChangeSet(localChangeSet);
    } else {
      this.bSnahKfDxl.warning(" Warning : KnowledgeAgent was requested to apply a Changeset, but no Changeset could be determined", paramResource);
    }
  }
  
  public void applyChangeSet(ChangeSet paramChangeSet)
  {
    synchronized (this.uHFZLTBWck)
    {
      this.GbWmmGEEYt.fireBeforeChangeSetApplied(paramChangeSet);
      this.bSnahKfDxl.info("KnowledgeAgent applying ChangeSet");
      SingleThreadKnowledgeAgent.ChangeSetState localChangeSetState = new SingleThreadKnowledgeAgent.ChangeSetState();
      localChangeSetState.bSnahKfDxl = this.meKrvCfWOQ;
      localChangeSetState.meKrvCfWOQ = (!this.vjYbNumBBE);
      processChangeSet(paramChangeSet, localChangeSetState);
      buildKnowledgeBase(localChangeSetState);
      this.GbWmmGEEYt.fireAfterChangeSetApplied(paramChangeSet);
    }
  }
  
  public void processChangeSet(Resource paramResource, SingleThreadKnowledgeAgent.ChangeSetState paramChangeSetState)
  {
    processChangeSet(getChangeSet(paramResource), paramChangeSetState);
  }
  
  public void processChangeSet(ChangeSet paramChangeSet, SingleThreadKnowledgeAgent.ChangeSetState paramChangeSetState)
  {
    synchronized (this.uHFZLTBWck)
    {
      this.GbWmmGEEYt.fireBeforeChangeSetProcessed(paramChangeSet);
      Iterator localIterator1 = paramChangeSet.getResourcesAdded().iterator();
      Resource localResource;
      Object localObject1;
      Object localObject2;
      while (localIterator1.hasNext())
      {
        localResource = (Resource)localIterator1.next();
        this.GbWmmGEEYt.fireBeforeResourceProcessed(paramChangeSet, localResource, ((InternalResource)localResource).getResourceType(), KnowledgeAgent.ResourceStatus.RESOURCE_ADDED);
        if (((InternalResource)localResource).getResourceType() == ResourceType.DSL)
        {
          try
          {
            muozJDZhHV(localResource);
          }
          catch (Exception localIOException1)
          {
            this.bSnahKfDxl.exception("KnowledgeAgent Fails trying to read DSL Resource: " + localResource, localIOException1);
          }
        }
        else if (((InternalResource)localResource).getResourceType() == ResourceType.CHANGE_SET)
        {
          this.bSnahKfDxl.debug("KnowledgeAgent processing sub ChangeSet=" + localResource);
          processChangeSet(localResource, paramChangeSetState);
        }
        else if ((paramChangeSetState.bSnahKfDxl) && (((InternalResource)localResource).isDirectory()))
        {
          this.muozJDZhHV.add(localResource);
          this.bSnahKfDxl.debug("KnowledgeAgent subscribing to directory=" + localResource);
          localObject1 = ((InternalResource)localResource).listResources().iterator();
          while (((Iterator)localObject1).hasNext())
          {
           localObject2 = (Resource)((Iterator)localObject1).next();
            if (!((InternalResource)localObject2).isDirectory())
            {
              ((InternalResource)localObject2).setResourceType(((InternalResource)localResource).getResourceType());
              addDefinitionMapping((Resource)localObject2, null, true);
              if ((addResourceMapping((Resource)localObject2, true)) && (paramChangeSetState.meKrvCfWOQ)) {
                paramChangeSetState.MEjnWwjnJJ.add((Resource) localObject2);
              }
            }
          }
        }
        else if ((addResourceMapping(localResource, true)) && (paramChangeSetState.meKrvCfWOQ))
        {
          paramChangeSetState.MEjnWwjnJJ.add(localResource);
        }
        this.GbWmmGEEYt.fireAfterResourceProcessed(paramChangeSet, localResource, ((InternalResource)localResource).getResourceType(), KnowledgeAgent.ResourceStatus.RESOURCE_ADDED);
      }
      localIterator1 = paramChangeSet.getResourcesRemoved().iterator();
      while (localIterator1.hasNext())
      {
        localResource = (Resource)localIterator1.next();
        this.GbWmmGEEYt.fireBeforeResourceProcessed(paramChangeSet, localResource, ((InternalResource)localResource).getResourceType(), KnowledgeAgent.ResourceStatus.RESOURCE_REMOVED);
        if (((InternalResource)localResource).getResourceType() == ResourceType.DSL)
        {
          this.CRxGNsliSY.remove(localResource);
        }
        else if (((InternalResource)localResource).getResourceType() == ResourceType.CHANGE_SET)
        {
          processChangeSet(localResource, paramChangeSetState);
        }
        else if ((paramChangeSetState.bSnahKfDxl) && (((InternalResource)localResource).isDirectory()))
        {
          this.bSnahKfDxl.debug("KnowledgeAgent unsubscribing from directory resource=" + localResource);
          this.muozJDZhHV.remove(localResource);
        }
        else
        {
        	Set<KnowledgeDefinition> localObject12 = removeResourceMapping(localResource, true);
          if ((localObject12 != null) && (paramChangeSetState.meKrvCfWOQ)) {
            paramChangeSetState.muozJDZhHV.put(localResource, localObject12);
          }
        }
        this.GbWmmGEEYt.fireAfterResourceProcessed(paramChangeSet, localResource, ((InternalResource)localResource).getResourceType(), KnowledgeAgent.ResourceStatus.RESOURCE_REMOVED);
      }
      localIterator1 = paramChangeSet.getResourcesModified().iterator();
      while (localIterator1.hasNext())
      {
        localResource = (Resource)localIterator1.next();
        this.GbWmmGEEYt.fireBeforeResourceProcessed(paramChangeSet, localResource, ((InternalResource)localResource).getResourceType(), KnowledgeAgent.ResourceStatus.RESOURCE_MODIFIED);
        if (((InternalResource)localResource).getResourceType() == ResourceType.DSL)
        {
          try
          {
            muozJDZhHV(localResource);
          }
          catch (Exception localIOException2)
          {
            this.bSnahKfDxl.exception("KnowledgeAgent Fails trying to read DSL Resource: " + localResource, localIOException2);
          }
        }
        else
        {
          if (((InternalResource)localResource).getResourceType() == ResourceType.CHANGE_SET) {
            continue;
          }
          if (((InternalResource)localResource).isDirectory())
          {
            if (this.muozJDZhHV.add(localResource)) {
              this.bSnahKfDxl.warning("KnowledgeAgent is subscribing to a modified directory=" + localResource + " when it should have already been subscribed");
            }
            Iterator localIterator2 = ((InternalResource)localResource).listResources().iterator();
            while (localIterator2.hasNext())
            {
              localObject2 = (Resource)localIterator2.next();
              if (!((InternalResource)localObject2).isDirectory()) {
                if (addResourceMapping((Resource)localObject2, true))
                {
                  ((InternalResource)localObject2).setResourceType(((InternalResource)localResource).getResourceType());
                  if (paramChangeSetState.meKrvCfWOQ) {
                    paramChangeSetState.MEjnWwjnJJ.add((Resource) localObject2);
                  }
                }
              }
            }
          }
          else
          {
            boolean bool = this.uHFZLTBWck.isResourceMapped(localResource);
            if (!bool)
            {
              this.bSnahKfDxl.warning("KnowledgeAgent subscribing to new resource=" + localResource + ", though it was marked as modified.");
              addResourceMapping(localResource, true);
              if (paramChangeSetState.meKrvCfWOQ) {
                paramChangeSetState.MEjnWwjnJJ.add(localResource);
              }
            }
            else
            {
            	Set<KnowledgeDefinition> localObject22 =removeResourceMapping(localResource, false);
              paramChangeSetState.jrtKEkxxdH.put(localResource, localObject22);
              addResourceMapping(localResource, false);
            }
          }
        }
        this.GbWmmGEEYt.fireAfterResourceProcessed(paramChangeSet, localResource, ((InternalResource)localResource).getResourceType(), KnowledgeAgent.ResourceStatus.RESOURCE_MODIFIED);
      }
      this.GbWmmGEEYt.fireAfterChangeSetProcessed(paramChangeSet, paramChangeSetState.MEjnWwjnJJ, paramChangeSetState.jrtKEkxxdH, paramChangeSetState.muozJDZhHV);
    }
  }
  
  public ChangeSet getChangeSet(Resource paramResource)
  {
    if (this.SrZgqlbHZb == null)
    {
      this.SrZgqlbHZb = new SemanticModules();
      this.SrZgqlbHZb.addSemanticModule(new ChangeSetSemanticModule());
    }
    XmlChangeSetReader localXmlChangeSetReader = new XmlChangeSetReader(this.SrZgqlbHZb, null, this.tkFQOWXdXf);
    if ((paramResource instanceof ClassPathResource)) {
      localXmlChangeSetReader.setClassLoader(((ClassPathResource)paramResource).getClassLoader(), null);
    } else {
      localXmlChangeSetReader.setClassLoader(((InternalRuleBase)((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase).getConfiguration().getClassLoader(), null);
    }
    ChangeSet localChangeSet = null;
    Reader localReader = null;
    try
    {
      localReader = paramResource.getReader();
      localChangeSet = localXmlChangeSetReader.read(localReader);
      if (localReader != null) {
        try
        {
          localReader.close();
        }
        catch (IOException localIOException1) {}
      }
      if (localChangeSet != null) {
        return localChangeSet;
      }
    }
    catch (Exception localException)
    {
      this.bSnahKfDxl.exception(new RuntimeException("Unable to parse ChangeSet", localException));
    }
    finally
    {
      if (localReader != null) {
        try
        {
          localReader.close();
        }
        catch (IOException localIOException3) {}
      }
    }
    this.bSnahKfDxl.exception(new RuntimeException("Unable to parse ChangeSet"));
    return localChangeSet;
  }
  
  private void MEjnWwjnJJ(Package paramPackage, Resource paramResource, boolean paramBoolean)
  {
    synchronized (this.uHFZLTBWck)
    {
      if ((!paramBoolean) && (paramResource == null))
      {
        this.bSnahKfDxl.warning("KnowledgeAgent: Impossible to map to a null resource! Use autoDiscoverResource = true ");
        return;
      }
      if ((paramBoolean) && (paramResource != null)) {
        this.bSnahKfDxl.warning("KnowledgeAgent: building resource map with resource set and autoDiscoverResource=true. Resource value wil be overwritten!");
      }
      for (Rule localObject3 : paramPackage.getRules())
      {
        if (paramBoolean) {
          paramResource = localObject3.getResource();
        }
        if (paramResource == null) {
          this.bSnahKfDxl.debug("KnowledgeAgent no resource mapped for rule=" + localObject3);
        }
        if (MEjnWwjnJJ(paramResource, localObject3)) {
          addDefinitionMapping(paramResource, localObject3, true);
        }
      }
      Iterator iterator = paramPackage.getRuleFlows().values().iterator();
      Object localObject2;
      while (((Iterator)iterator).hasNext())
      {
        localObject2 = (Process)((Iterator)iterator).next();
        if (paramResource == null) {
          this.bSnahKfDxl.debug("KnowledgeAgent no resource mapped for process=" + localObject2);
        }
        if (paramBoolean) {
          paramResource = ((ResourcedObject)localObject2).getResource();
        }
        if (MEjnWwjnJJ(paramResource, (KnowledgeDefinition)localObject2)) {
          addDefinitionMapping(paramResource, (KnowledgeDefinition)localObject2, true);
        }
      }
      Iterator iterator2 = paramPackage.getTypeDeclarations().values().iterator();
      while (((Iterator)iterator2).hasNext())
      {
        localObject2 = (TypeDeclaration)((Iterator)iterator2).next();
        if (paramResource == null) {
          this.bSnahKfDxl.debug("KnowledgeAgent no resource mapped for type=" + localObject2);
        }
        if (paramBoolean) {
          paramResource = ((TypeDeclaration)localObject2).getResource();
        }
        if (MEjnWwjnJJ(paramResource, (KnowledgeDefinition)localObject2)) {
          addDefinitionMapping(paramResource, (KnowledgeDefinition)localObject2, true);
        }
      }
      Iterator iterator3= paramPackage.getFunctions().values().iterator();
      while (((Iterator)iterator3).hasNext())
      {
        localObject2 = (Function)((Iterator)iterator3).next();
        if ((paramResource != null) && (!((InternalResource)paramResource).hasURL())) {
          this.bSnahKfDxl.debug("KnowledgeAgent no resource mapped for function=" + localObject2);
        }
        if (paramBoolean) {
          paramResource = ((Function)localObject2).getResource();
        }
        if (MEjnWwjnJJ(paramResource, (KnowledgeDefinition)localObject2)) {
          addDefinitionMapping(paramResource, (KnowledgeDefinition)localObject2, true);
        }
      }
    }
  }
  
  private boolean MEjnWwjnJJ(Resource paramResource, KnowledgeDefinition paramKnowledgeDefinition)
  {
    return (!this.uHFZLTBWck.isResourceMapped(paramResource)) || (!this.uHFZLTBWck.getDefinitions(paramResource).contains(paramKnowledgeDefinition));
  }
  
  public void autoBuildResourceMapping()
  {
    this.bSnahKfDxl.debug("KnowledgeAgent building resource map");
    synchronized (this.uHFZLTBWck)
    {
      RuleBase localRuleBase = ((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase;
      for (Package localPackage : localRuleBase.getPackages()) {
        MEjnWwjnJJ(localPackage, null, true);
      }
    }
  }
  
  public KnowledgeBase getKnowledgeBase()
  {
    synchronized (this.uHFZLTBWck)
    {
      return this.jrtKEkxxdH;
    }
  }
  
  public StatelessKnowledgeSession newStatelessKnowledgeSession()
  {
    return new StatelessKnowledgeSessionImpl(null, this, null);
  }
  
  public StatelessKnowledgeSession newStatelessKnowledgeSession(KnowledgeSessionConfiguration paramKnowledgeSessionConfiguration)
  {
    return new StatelessKnowledgeSessionImpl(null, this, paramKnowledgeSessionConfiguration);
  }
  
  public void resourcesChanged(ChangeSet paramChangeSet)
  {
    try
    {
      this.bSnahKfDxl.debug("KnowledgeAgent received ChangeSet changed notification");
      this.beEncABYgg.put(paramChangeSet);
    }
    catch (InterruptedException localInterruptedException)
    {
      this.bSnahKfDxl.exception(new RuntimeException("KnowledgeAgent error while adding ChangeSet notification to queue", localInterruptedException));
    }
  }
  
  public void buildKnowledgeBase(SingleThreadKnowledgeAgent.ChangeSetState paramChangeSetState)
  {
    this.bSnahKfDxl.debug("KnowledgeAgent rebuilding KnowledgeBase using ChangeSet");
    synchronized (this.uHFZLTBWck)
    {
      if (this.vjYbNumBBE) {
        MEjnWwjnJJ(paramChangeSetState);
      } else {
        muozJDZhHV(paramChangeSetState);
      }
      InternalRuleBase localInternalRuleBase = (InternalRuleBase)((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase;
      localInternalRuleBase.lock();
      try
      {
        if (localInternalRuleBase.getConfiguration().isSequential()) {
          localInternalRuleBase.getReteooBuilder().order();
        }
      }
      finally
      {
        localInternalRuleBase.unlock();
      }
    }
    this.GbWmmGEEYt.fireKnowledgeBaseUpdated(this.jrtKEkxxdH);
    this.bSnahKfDxl.debug("KnowledgeAgent finished rebuilding KnowledgeBase using ChangeSet");
  }
  
  private Collection<KnowledgePackage> MEjnWwjnJJ(Resource paramResource)
  {
    return MEjnWwjnJJ(paramResource);
  }
  
  private Collection<KnowledgePackage> MEjnWwjnJJ(Resource paramResource, KnowledgeBuilder paramKnowledgeBuilder)
  {
    if (((InternalResource)paramResource).getResourceType() != ResourceType.PKG)
    {
      if (paramKnowledgeBuilder == null) {
        paramKnowledgeBuilder = MEjnWwjnJJ();
      }
      paramKnowledgeBuilder.add(paramResource, ((InternalResource)paramResource).getResourceType());
      if (paramKnowledgeBuilder.hasErrors())
      {
        this.GbWmmGEEYt.fireResourceCompilationFailed(paramKnowledgeBuilder, paramResource, ((InternalResource)paramResource).getResourceType());
        this.bSnahKfDxl.warning("KnowledgeAgent has KnowledgeBuilder errors ", paramKnowledgeBuilder.getErrors());
      }
      if (paramKnowledgeBuilder.getKnowledgePackages().iterator().hasNext()) {
        return paramKnowledgeBuilder.getKnowledgePackages();
      }
      return Collections.emptyList();
    }
    InputStream localInputStream = null;
    Object localObject1 = null;
    try
    {
      localInputStream = paramResource.getInputStream();
      CompositeClassLoader localCompositeClassLoader = null;
      if (isUseKBaseClassLoaderForCompiling()) {
        localCompositeClassLoader = ((InternalRuleBase)((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase).getRootClassLoader();
      } else if (this.iJgXNHlULI != null) {
        this.bSnahKfDxl.warning("Even if a custom KnowledgeBuilderConfiguration was provided,  the Knowledge Agent will not use any specific classloader while deserializing packages. Expect ClassNotFoundExceptions.");
      }
      Object localObject2 = DroolsStreamUtils.streamIn(localInputStream, localCompositeClassLoader);
      if ((localObject2 instanceof Collection))
      {
        localObject1 = (Collection)localObject2;
      }
      else if ((localObject2 instanceof KnowledgePackageImp))
      {
        localObject1 = Collections.singletonList((KnowledgePackage)localObject2);
      }
      else if ((localObject2 instanceof Package))
      {
        localObject1 = Collections.singletonList(new KnowledgePackageImp((Package)localObject2));
      }
      else if ((localObject2 instanceof Package[]))
      {
        localObject1 = new ArrayList();
        for (Package localPackage : (Package[])localObject2) {
          ((Collection)localObject1).add(new KnowledgePackageImp(localPackage));
        }
      }
      else
      {
        throw new RuntimeException("Unknown binary format trying to load resource " + paramResource.toString());
      }
      Iterator iterator = ((Collection)localObject1).iterator();
      while (((Iterator)iterator).hasNext())
      {
        KnowledgePackage localKnowledgePackage = (KnowledgePackage)((Iterator)iterator).next();
        for (Rule localObject6 : ((KnowledgePackageImp)localKnowledgePackage).pkg.getRules()) {
          localObject6.setResource(paramResource);
        }
        iterator = ((KnowledgePackageImp)localKnowledgePackage).pkg.getRuleFlows().values().iterator();
        Object localObject5;
        while (((Iterator)iterator).hasNext())
        {
          localObject5 = (Process)((Iterator)iterator).next();
          ((ResourcedObject)localObject5).setResource(paramResource);
        }
        iterator = ((KnowledgePackageImp)localKnowledgePackage).pkg.getTypeDeclarations().values().iterator();
        while (((Iterator)iterator).hasNext())
        {
          localObject5 = (TypeDeclaration)((Iterator)iterator).next();
          ((TypeDeclaration)localObject5).setResource(paramResource);
        }
      }
      return (Collection<KnowledgePackage>) localObject1;
    }
    catch (Exception localException)
    {
      this.bSnahKfDxl.exception(new RuntimeException("KnowledgeAgent exception while trying to deserialize KnowledgeDefinitionsPackage  ", localException));
    }
    finally
    {
      try
      {
        if (localInputStream != null) {
          localInputStream.close();
        }
      }
      catch (IOException localIOException3)
      {
        this.bSnahKfDxl.exception(new RuntimeException("KnowledgeAgent exception while trying to close KnowledgeDefinitionsPackage  ", localIOException3));
      }
    }
	return null;
  }
  
  private void MEjnWwjnJJ(SingleThreadKnowledgeAgent.ChangeSetState paramChangeSetState)
  {
    if (!this.vjYbNumBBE) {
      this.bSnahKfDxl.warning("KnowledgeAgent rebuilding KnowledgeBase when newInstance is false");
    }
    if (this.jrtKEkxxdH != null) {
      this.jrtKEkxxdH = KnowledgeBaseFactory.newKnowledgeBase(((InternalRuleBase)((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase).getConfiguration());
    } else {
      this.jrtKEkxxdH = KnowledgeBaseFactory.newKnowledgeBase();
    }
    paramChangeSetState.MEjnWwjnJJ.clear();
    Iterator localIterator = this.uHFZLTBWck.getAllResources().iterator();
    while (localIterator.hasNext())
    {
      Resource localResource = (Resource)localIterator.next();
      if ((!(localResource instanceof ReaderResource)) || (((ReaderResource)localResource).getReader() != null)) {
        paramChangeSetState.MEjnWwjnJJ.add(localResource);
      }
    }
    jrtKEkxxdH(paramChangeSetState);
    this.bSnahKfDxl.info("KnowledgeAgent new KnowledgeBase now built and in use");
  }
  
  private void muozJDZhHV(SingleThreadKnowledgeAgent.ChangeSetState paramChangeSetState)
  {
    if (this.vjYbNumBBE) {
      this.bSnahKfDxl.warning("KnowledgeAgent incremental build of KnowledgeBase when newInstance is true");
    }
    KnowledgeBuilder localKnowledgeBuilder = MEjnWwjnJJ();
    synchronized (this.uHFZLTBWck)
    {
      this.bSnahKfDxl.info("KnowledgeAgent performing an incremental build of the ChangeSet");
      if (this.jrtKEkxxdH == null) {
        this.jrtKEkxxdH = KnowledgeBaseFactory.newKnowledgeBase();
      }
      Iterator localIterator = paramChangeSetState.muozJDZhHV.entrySet().iterator();
      Object localObject1;
      Object localObject2;
      Object localObject3;
      while (localIterator.hasNext())
      {
        localObject1 = (Map.Entry)localIterator.next();
        localObject2 = ((Set)((Map.Entry)localObject1).getValue()).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject3 = (KnowledgeDefinition)((Iterator)localObject2).next();
          MEjnWwjnJJ((KnowledgeDefinition)localObject3);
        }
      }
      localIterator = paramChangeSetState.jrtKEkxxdH.entrySet().iterator();
      while (localIterator.hasNext())
      {
        localObject1 = (Map.Entry)localIterator.next();
        localObject2 = MEjnWwjnJJ((Resource)((Map.Entry)localObject1).getKey());
        MEjnWwjnJJ((Map.Entry)localObject1, (Collection)localObject2);
        if (localObject2 != null)
        {
          localObject3 = ((Collection)localObject2).iterator();
          while (((Iterator)localObject3).hasNext())
          {
            KnowledgePackage localKnowledgePackage = (KnowledgePackage)((Iterator)localObject3).next();
            KnowledgePackageImp localKnowledgePackageImp1 = (KnowledgePackageImp)localKnowledgePackage;
            Set localSet = MEjnWwjnJJ((Map.Entry)localObject1, localKnowledgePackageImp1);
            KnowledgePackageImp localKnowledgePackageImp2 = (KnowledgePackageImp)this.jrtKEkxxdH.getKnowledgePackage(localKnowledgePackageImp1.getName());
            AbstractRuleBase localAbstractRuleBase = (AbstractRuleBase)((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase;
            CompositeClassLoader localCompositeClassLoader = localAbstractRuleBase.getRootClassLoader();
            JavaDialectRuntimeData.TypeDeclarationClassLoader localTypeDeclarationClassLoader = (JavaDialectRuntimeData.TypeDeclarationClassLoader)((AbstractRuleBase)((KnowledgeBaseImpl)this.jrtKEkxxdH).ruleBase).getTypeDeclarationClassLoader();
            localKnowledgePackageImp1.pkg.getDialectRuntimeRegistry().onAdd(localCompositeClassLoader);
            localKnowledgePackageImp1.pkg.getDialectRuntimeRegistry().onBeforeExecute();
            localKnowledgePackageImp1.pkg.getClassFieldAccessorStore().setClassFieldAccessorCache(localAbstractRuleBase.getClassFieldAccessorCache());
            localKnowledgePackageImp1.pkg.getClassFieldAccessorStore().wire();
            this.bSnahKfDxl.debug("KnowledgeAgent: Diffing: " + ((Map.Entry)localObject1).getKey());
            BinaryResourceDiffProducerImpl localBinaryResourceDiffProducerImpl = new BinaryResourceDiffProducerImpl();
            ResourceDiffResult localResourceDiffResult = localBinaryResourceDiffProducerImpl.diff(localSet, localKnowledgePackageImp1, localKnowledgePackageImp2);
            Object localObject4 = localResourceDiffResult.getRemovedDefinitions().iterator();
            KnowledgeDefinition localKnowledgeDefinition;
            while (((Iterator)localObject4).hasNext())
            {
              localKnowledgeDefinition = (KnowledgeDefinition)((Iterator)localObject4).next();
              this.bSnahKfDxl.debug("KnowledgeAgent: Removing: " + localKnowledgeDefinition);
              MEjnWwjnJJ(localKnowledgeDefinition);
            }
            localObject4 = localResourceDiffResult.getUnmodifiedDefinitions().iterator();
            while (((Iterator)localObject4).hasNext())
            {
              localKnowledgeDefinition = (KnowledgeDefinition)((Iterator)localObject4).next();
              addDefinitionMapping((Resource)((Map.Entry)localObject1).getKey(), localKnowledgeDefinition, false);
            }
            localObject4 = (Set)paramChangeSetState.vjYbNumBBE.get(((Map.Entry)localObject1).getKey());
            if (localObject4 == null)
            {
              localObject4 = new HashSet();
              paramChangeSetState.vjYbNumBBE.put((Resource)((Map.Entry)localObject1).getKey(), (Set<KnowledgePackage>) localObject4);
            }
            ((Set)localObject4).add(localResourceDiffResult.getPkg());
          }
        }
      }
      localIterator = paramChangeSetState.MEjnWwjnJJ.iterator();
      while (localIterator.hasNext())
      {
        localObject1 = (Resource)localIterator.next();
        localObject2 =MEjnWwjnJJ((Resource)localObject1, localKnowledgeBuilder);
        if ((localObject2 == null) || (((Collection)localObject2).isEmpty())) {
          this.bSnahKfDxl.warning("KnowledgeAgent: The resource didn't create any package: " + localObject1);
        } else {
        	//已修改
          paramChangeSetState.vjYbNumBBE.put((Resource)localObject1, (Set<KnowledgePackage>)localObject2);
        }
      }
      paramChangeSetState.MEjnWwjnJJ.clear();
      paramChangeSetState.jrtKEkxxdH.clear();
      jrtKEkxxdH(paramChangeSetState);
    }
    this.bSnahKfDxl.info("KnowledgeAgent incremental build of KnowledgeBase finished and in use");
  }
  
  private void MEjnWwjnJJ(Map.Entry<Resource, Set<KnowledgeDefinition>> paramEntry, Collection<KnowledgePackage> paramCollection)
  {
    HashSet localHashSet = new HashSet();
    Object localObject;
    if ((paramCollection == null) || (paramCollection.isEmpty()))
    {
      this.bSnahKfDxl.warning("KnowledgeAgent: The resource didn't create any package: " + paramEntry.getKey() + ". Removing any existing knowledge definition of " + paramEntry.getKey());
    }
    else
    {
    	Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        localObject = (KnowledgePackage)localIterator.next();
        localHashSet.add(((KnowledgePackage)localObject).getName());
      }
    }
    Iterator localIterator = ((Set)paramEntry.getValue()).iterator();
    while (localIterator.hasNext())
    {
      localObject = (KnowledgeDefinition)localIterator.next();
      if (!localHashSet.contains(((KnowledgeDefinition)localObject).getNamespace()))
      {
        this.bSnahKfDxl.debug("KnowledgeAgent: Removing: " + localObject);
        MEjnWwjnJJ((KnowledgeDefinition)localObject);
      }
    }
  }
  
  private Set<KnowledgeDefinition> MEjnWwjnJJ(Map.Entry<Resource, Set<KnowledgeDefinition>> paramEntry, KnowledgePackageImp paramKnowledgePackageImp)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = ((Set)paramEntry.getValue()).iterator();
    while (localIterator.hasNext())
    {
      KnowledgeDefinition localKnowledgeDefinition = (KnowledgeDefinition)localIterator.next();
      if (localKnowledgeDefinition.getNamespace().equals(paramKnowledgePackageImp.getName())) {
        localHashSet.add(localKnowledgeDefinition);
      }
    }
    return localHashSet;
  }
  
  private void MEjnWwjnJJ(KnowledgeDefinition paramKnowledgeDefinition)
  {
    try
    {
      Object localObject;
      if ((paramKnowledgeDefinition instanceof Query))
      {
        localObject = (Query)paramKnowledgeDefinition;
        this.bSnahKfDxl.debug("KnowledgeAgent removing Query=" + localObject + " from package=" + ((Query)localObject).getPackageName());
        this.jrtKEkxxdH.removeQuery(((Query)localObject).getPackageName(), ((Query)localObject).getName());
      }
      else if ((paramKnowledgeDefinition instanceof Rule))
      {
        localObject = (Rule)paramKnowledgeDefinition;
        this.bSnahKfDxl.debug("KnowledgeAgent removing Rule=" + localObject + " from package=" + ((Rule)localObject).getPackageName());
        this.jrtKEkxxdH.removeRule(((Rule)localObject).getPackageName(), ((Rule)localObject).getName());
      }
      else if ((paramKnowledgeDefinition instanceof Process))
      {
        localObject = (Process)paramKnowledgeDefinition;
        this.bSnahKfDxl.debug("KnowledgeAgent removing Process=" + localObject);
        this.jrtKEkxxdH.removeProcess(((Process)localObject).getId());
      }
      else if ((!(paramKnowledgeDefinition instanceof TypeDeclaration)) && ((paramKnowledgeDefinition instanceof Function)))
      {
        localObject = (Function)paramKnowledgeDefinition;
        this.jrtKEkxxdH.removeFunction(((Function)localObject).getNamespace(), ((Function)localObject).getName());
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      this.bSnahKfDxl.warning(localIllegalArgumentException.getMessage());
    }
  }
  
  private void jrtKEkxxdH(SingleThreadKnowledgeAgent.ChangeSetState paramChangeSetState)
  {
	    Object localObject2;
	    Object localObject3;
	    Iterator iterator44=null;
	    if (!paramChangeSetState.MEjnWwjnJJ.isEmpty())
	    {
	       KnowledgeBuilder  localObject1 = MEjnWwjnJJ();
	       Iterator  localIterator = paramChangeSetState.MEjnWwjnJJ.iterator();
	      while (localIterator.hasNext())
	      {
	        localObject2 = (Resource)localIterator.next();
	        localObject3 = MEjnWwjnJJ((Resource)localObject2, (KnowledgeBuilder)localObject1);
	        Object localObject4 = (Set)paramChangeSetState.vjYbNumBBE.get(localObject2);
	        if (localObject4 == null)
	        {
	          localObject4 = new HashSet();
	          paramChangeSetState.vjYbNumBBE.put((Resource)localObject2, (Set<KnowledgePackage>)localObject4);
	        }
	        ((Set)localObject4).addAll((Collection)localObject3);
	      }
	    }
	    Object localObject1 = new LinkedHashSet();
	    Iterator localIterator = paramChangeSetState.vjYbNumBBE.keySet().iterator();
	    while (localIterator.hasNext())
	    {
	      localObject2 = (Resource)localIterator.next();
	      ((Set)localObject1).addAll((Collection)paramChangeSetState.vjYbNumBBE.get(localObject2));
	    }
	    try
	    {
	      this.jrtKEkxxdH.addKnowledgePackages((Collection)localObject1);
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      this.bSnahKfDxl.exception(localRuntimeException);
	      this.bSnahKfDxl.warning("Runtime error while updating Knowledge Base, packages may be in an inconsistent state and will be removed ");
	      iterator44 = ((Set)localObject1).iterator();
	    }
	    while (iterator44!=null&&((Iterator)iterator44).hasNext())
	    {
	      localObject3 = (KnowledgePackage)((Iterator)iterator44).next();
	      this.bSnahKfDxl.warning("- Removing package " + ((KnowledgePackage)localObject3).getName());
	      this.jrtKEkxxdH.removeKnowledgePackage(((KnowledgePackage)localObject3).getName());
	    }
	    autoBuildResourceMapping();
  }
  
  public String getName()
  {
    return this.MEjnWwjnJJ;
  }
  
  public boolean addResourceMapping(Resource paramResource, boolean paramBoolean)
  {
    boolean bool = this.uHFZLTBWck.createNewResourceEntry(paramResource);
    if ((paramBoolean) && (bool) && (((InternalResource)paramResource).exists()))
    {
      this.bSnahKfDxl.debug("KnowledgeAgent notifier subscribing to resource=" + paramResource);
      return true;
    }
    return false;
  }
  
//  public Map<Resource, Set<KnowledgeDefinition>> getRegisteredResources()
//  {
//	  return SingleThreadKnowledgeAgent.RegisteredResourceMap.MEjnWwjnJJ(this.uHFZLTBWck);
//  }
  
  public boolean addDefinitionMapping(Resource paramResource, KnowledgeDefinition paramKnowledgeDefinition, boolean paramBoolean)
  {
    if (paramResource == null)
    {
      this.bSnahKfDxl.warning("KnowledgeAgent: impossible to add a map for a null resource! skiping.");
      return false;
    }
    this.bSnahKfDxl.debug("KnowledgeAgent mapping resource=" + paramResource + " to KnowledgeDefinition=" + paramKnowledgeDefinition);
    int i = !this.uHFZLTBWck.isResourceMapped(paramResource) ? 1 : 0;
    if (((paramResource instanceof ClassPathResource)) && (((ClassPathResource)paramResource).getClassLoader() == null)) {
      ((ClassPathResource)paramResource).setClassLoader(((InternalRuleBase)((InternalKnowledgeBase)this.jrtKEkxxdH).getRuleBase()).getRootClassLoader());
    }
    boolean bool = true;
    if (paramKnowledgeDefinition != null) {
      bool = this.uHFZLTBWck.putDefinition(paramResource, paramKnowledgeDefinition);
    }
    if ((paramBoolean) && (i != 0) && (((InternalResource)paramResource).exists())) {
      this.bSnahKfDxl.debug("KnowledgeAgent notifier subscribing to resource=" + paramResource);
    }
    return bool;
  }
  
  public Set<KnowledgeDefinition> removeResourceMapping(Resource paramResource, boolean paramBoolean)
  {
    this.bSnahKfDxl.debug("KnowledgeAgent removing mappings for resource=" + paramResource + " with unsubscribe=" + paramBoolean);
    Set localSet = this.uHFZLTBWck.removeDefinitions(paramResource);
    if ((localSet != null) && (paramBoolean)) {
      this.bSnahKfDxl.debug("KnowledgeAgent notifier unsubscribing to resource=" + paramResource);
    }
    return localSet;
  }
  
  private KnowledgeBuilder MEjnWwjnJJ()
  {
    if (this.uHFZLTBWck.onlyHasPKGResources()) {
      return null;
    }
    KnowledgeBuilder localKnowledgeBuilder;
    if (this.iJgXNHlULI != null) {
      localKnowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(this.iJgXNHlULI);
    } else if (isUseKBaseClassLoaderForCompiling()) {
      localKnowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(this.jrtKEkxxdH);
    } else {
      localKnowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    }
    if (this.CRxGNsliSY != null)
    {
      Iterator localIterator = this.CRxGNsliSY.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localKnowledgeBuilder.add(ResourceFactory.newByteArrayResource(((String)localEntry.getValue()).getBytes()), ResourceType.DSL);
      }
    }
    return localKnowledgeBuilder;
  }
  
  private void muozJDZhHV(Resource paramResource) throws IOException
  {
    BufferedReader localBufferedReader = null;
    try
    {
      localBufferedReader = new BufferedReader(paramResource.getReader());
      StringBuilder localStringBuilder = new StringBuilder();
      String str;
      while ((str = localBufferedReader.readLine()) != null)
      {
        localStringBuilder.append(str);
        localStringBuilder.append("\n");
      }
      this.CRxGNsliSY.put(paramResource, localStringBuilder.toString());
    }
    finally
    {
      if (localBufferedReader != null) {
        localBufferedReader.close();
      }
    }
  }
  
  public void addEventListener(KnowledgeAgentEventListener paramKnowledgeAgentEventListener)
  {
    this.GbWmmGEEYt.addEventListener(paramKnowledgeAgentEventListener);
  }
  
  public void removeEventListener(KnowledgeAgentEventListener paramKnowledgeAgentEventListener)
  {
    this.GbWmmGEEYt.removeEventListener(paramKnowledgeAgentEventListener);
  }
  
  public void dispose()
  {
    synchronized (this.uHFZLTBWck)
    {
      if (this.jrtKEkxxdH != null)
      {
        Collection localCollection = this.jrtKEkxxdH.getStatefulKnowledgeSessions();
        if ((localCollection != null) && (!localCollection.isEmpty()))
        {
          String str = "The kbase still contains " + localCollection.size() + " stateful sessions. You must dispose them first.";
          this.bSnahKfDxl.warning(str);
          throw new IllegalStateException(str);
        }
      }
    }
  }
  
  protected void finalize()
  {
    try {
		super.finalize();
	} catch (Throwable e) {
		e.printStackTrace();
	}
    if (this.VCYfbOySbP != null) {}
  }
  
  public void monitorResourceChangeEvents(boolean paramBoolean)
  {
    HashSet localHashSet = new HashSet();
    localHashSet.addAll(this.muozJDZhHV);
    localHashSet.addAll(this.uHFZLTBWck.getAllResources());
    localHashSet.addAll(this.CRxGNsliSY.keySet());
    Iterator localIterator = localHashSet.iterator();
    while (localIterator.hasNext())
    {
      Resource localResource = (Resource)localIterator.next();
      if (paramBoolean) {
        this.bSnahKfDxl.debug("KnowledgeAgent subscribing from resource=" + localResource);
      } else {
        this.bSnahKfDxl.debug("KnowledgeAgent unsubscribing from resource=" + localResource);
      }
    }
    if ((!paramBoolean) && (this.VCYfbOySbP != null))
    {
      this.VCYfbOySbP.stop();
      this.MsYfPmdGXp.cancel(true);
      this.VCYfbOySbP = null;
    }
    else if ((!paramBoolean) || (this.VCYfbOySbP != null)) {}
  }
  
  //1
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
      Set<KnowledgeDefinition> localObject = (Set)this.MEjnWwjnJJ.get(paramResource);
      if (localObject == null)
      {
        localObject = new HashSet();
        this.MEjnWwjnJJ.put(paramResource, localObject);
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
      Set<KnowledgeDefinition> localObject = (Set)this.MEjnWwjnJJ.get(paramResource);
      if ((paramBoolean) && (localObject == null)) {
        localObject = new HashSet();
      }
      return localObject;
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
  
  //2
  public class ChangeSetState
  {
    List<Resource> MEjnWwjnJJ = new ArrayList<Resource>();
    Map<Resource, Set<KnowledgeDefinition>> muozJDZhHV = new HashMap<Resource, Set<KnowledgeDefinition>>();
    Map<Resource, Set<KnowledgeDefinition>> jrtKEkxxdH = new HashMap<Resource, Set<KnowledgeDefinition>>();
    Map<Resource, Set<KnowledgePackage>> vjYbNumBBE = new LinkedHashMap<Resource, Set<KnowledgePackage>>();
    boolean bSnahKfDxl;
    boolean meKrvCfWOQ;
  }

}
