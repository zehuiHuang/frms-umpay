package cn.com.umpay.frms.preprocessor.core;

import cn.com.bsfit.frms.obj.MemCachedItem;
import java.util.Collection;
import java.util.List;

public abstract interface ChangeSetHandler
{
  public abstract Collection<MemCachedItem> onChange(List<Object> paramList, Collection<MemCachedItem> paramCollection);
}
