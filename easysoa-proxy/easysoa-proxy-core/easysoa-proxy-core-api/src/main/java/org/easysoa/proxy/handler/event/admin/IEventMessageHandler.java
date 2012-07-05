package org.easysoa.proxy.handler.event.admin;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author fntangke
 * 
 */

public interface IEventMessageHandler {
    
    /**
     * WARNING readonly
     * @param void
     * @return the services to call
     */
    public Map<String, List<String>> getListenedServiceUrlToServicesToLaunchUrlMap();	
  
      
    /**
     * Update the conf ; WARNING synchronized method
     * @param listenedServiceUrlToServicesToLaunchUrlMap  services to call
     * @return void
     */
    
    public void setListenedServiceUrlToServicesToLaunchUrlMap(
			Map<String, List<String>> listenedServiceUrlToServicesToLaunchUrlMap);
}
