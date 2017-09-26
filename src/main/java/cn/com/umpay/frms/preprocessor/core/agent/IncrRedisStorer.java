package cn.com.umpay.frms.preprocessor.core.agent;

import cn.com.bsfit.frms.base.store.CachedItemStorer;
import cn.com.bsfit.frms.obj.MemCachedItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;

public class IncrRedisStorer
  implements CachedItemStorer
{
  private static final Logger MEjnWwjnJJ = LoggerFactory.getLogger(IncrRedisStorer.class);
  private JedisSentinelPool muozJDZhHV;
  
  public void setPool(JedisSentinelPool paramJedisSentinelPool)
  {
    this.muozJDZhHV = paramJedisSentinelPool;
  }
  
  public void storeMemCachedItem(Collection<MemCachedItem> paramCollection)
  {
    Jedis localJedis = null;
    try
    {
      localJedis = this.muozJDZhHV.getResource();
      Pipeline localPipeline = localJedis.pipelined();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        MemCachedItem localMemCachedItem = (MemCachedItem)localIterator.next();
        try
        {
          byte[] arrayOfByte = localMemCachedItem.getMemCachedKey().getBytes();
          localPipeline.set(arrayOfByte, SerializationUtils.serialize(localMemCachedItem));
          if (localMemCachedItem.get("transDate") != null) {
            localPipeline.expire(arrayOfByte, 864000);
          } else if (localMemCachedItem.get("expire") != null) {
            localPipeline.expire(arrayOfByte, Integer.valueOf(localMemCachedItem.get("expire").toString()).intValue() * 24 * 3600);
          } else {
            localPipeline.expire(arrayOfByte, 7776000);
          }
        }
        catch (Exception localException2)
        {
          MEjnWwjnJJ.error("Save memcached error", localException2);
        }
        MEjnWwjnJJ.info("MemCachedItem-id[{}],[{}]", localMemCachedItem.getMemCachedKey(), localMemCachedItem);
      }
      localPipeline.sync();
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      if (null != localJedis)
      {
        this.muozJDZhHV.returnBrokenResource(localJedis);
        localJedis = null;
      }
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
    finally
    {
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
  }
  
  public List<MemCachedItem> get(List<byte[]> paramList)
  {
    Jedis localJedis = null;
    try
    {
      localJedis = this.muozJDZhHV.getResource();
      ArrayList localArrayList = new ArrayList();
      byte[][] arrayOfByte = new byte[paramList.size()][];
      List localList = localJedis.mget((byte[][])paramList.toArray(arrayOfByte));
      Object localObject1 = localList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        byte[] arrayOfByte1 = (byte[])((Iterator)localObject1).next();
        if ((arrayOfByte1 != null) && (SerializationUtils.deserialize(arrayOfByte1) != null)) {
          localArrayList.add((MemCachedItem)SerializationUtils.deserialize(arrayOfByte1));
        }
      }
      if ((localArrayList != null) && (localArrayList.size() > 0))
      {
        localObject1 = localArrayList;
        return (List<MemCachedItem>) localObject1;
      }
      localObject1 = null;
      return (List<MemCachedItem>) localObject1;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (null != localJedis)
      {
        this.muozJDZhHV.returnBrokenResource(localJedis);
        localJedis = null;
      }
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
    finally
    {
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
    return null;
  }
  
  public void delete(String paramString)
  {
    Jedis localJedis = null;
    try
    {
      localJedis = this.muozJDZhHV.getResource();
      localJedis.del(paramString);
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (null != localJedis)
      {
        this.muozJDZhHV.returnBrokenResource(localJedis);
        localJedis = null;
      }
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
    finally
    {
      if (null != localJedis) {
        this.muozJDZhHV.returnResource(localJedis);
      }
    }
  }
}
