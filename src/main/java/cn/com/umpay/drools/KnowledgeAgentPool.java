package cn.com.umpay.drools;

import java.util.ArrayList;
import java.util.Iterator;
import org.drools.ChangeSet;

public class KnowledgeAgentPool
  extends ArrayList<SingleThreadKnowledgeAgent>
{
  private int MEjnWwjnJJ = 0;
  private static final long muozJDZhHV = 1L;
  
  public void applyChangeSet(ChangeSet paramChangeSet)
  {
    synchronized (this)
    {
      Iterator localIterator = iterator();
      while (localIterator.hasNext())
      {
        SingleThreadKnowledgeAgent localSingleThreadKnowledgeAgent = (SingleThreadKnowledgeAgent)localIterator.next();
        localSingleThreadKnowledgeAgent.applyChangeSet(paramChangeSet);
      }
    }
  }
  
  public SingleThreadKnowledgeAgent get()
  {
    this.MEjnWwjnJJ += 1;
    if (this.MEjnWwjnJJ == size()) {
      this.MEjnWwjnJJ = 0;
    }
    return (SingleThreadKnowledgeAgent)get(this.MEjnWwjnJJ);
  }
  
  public KnowledgeAgentPool() {}
  
  public KnowledgeAgentPool(int paramInt)
  {
    super(paramInt);
  }
}
