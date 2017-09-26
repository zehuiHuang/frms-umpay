package cn.com.umpay.frms.preprocessor.core;

import cn.com.bsfit.frms.obj.MemCachedItem;
import cn.com.bsfit.frms.obj.TaggedID;
import java.util.List;

public abstract interface ChangeSetAgent
{
  public abstract List<MemCachedItem> applyChangeSet(List<TaggedID> paramList);
  
  public abstract List<MemCachedItem> applyMemCachedItem(List<MemCachedItem> paramList, List<Object> paramList1);
}
