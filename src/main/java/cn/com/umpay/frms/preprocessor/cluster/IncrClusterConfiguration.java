package cn.com.umpay.frms.preprocessor.cluster;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncrClusterConfiguration
  extends ReceiverAdapter
{
  public static AtomicInteger CLUSTER_SIZE = new AtomicInteger(1);
  public static AtomicInteger CLUSTER_INDEX = new AtomicInteger(-1);
  private static final Logger MEjnWwjnJJ = LoggerFactory.getLogger(IncrClusterConfiguration.class);
  private JChannel muozJDZhHV;
  private String jrtKEkxxdH;
  
  public void start() throws Exception
  {
    this.muozJDZhHV = new JChannel();
    this.muozJDZhHV.setReceiver(this);
    this.muozJDZhHV.connect(this.jrtKEkxxdH);
  }
  
  public void stop()
  {
    if (this.muozJDZhHV != null) {
      this.muozJDZhHV.close();
    }
  }
  
  public void viewAccepted(View paramView)
  {
    MEjnWwjnJJ.info("view accepted: " + paramView);
    CLUSTER_SIZE.set(paramView.size());
    Address localAddress = this.muozJDZhHV.getAddress();
    MEjnWwjnJJ.info("address of current node: " + localAddress.toString());
    CLUSTER_INDEX.set(paramView.getMembers().indexOf(localAddress));
    MEjnWwjnJJ.info("cluster size: {}, index of current node: {}", Integer.valueOf(CLUSTER_SIZE.get()), Integer.valueOf(CLUSTER_INDEX.get()));
  }
  
  public void setClusterName(String paramString)
  {
    this.jrtKEkxxdH = paramString;
  }
}
