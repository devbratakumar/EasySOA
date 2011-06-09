package com.openwide.easysoa.esperpoc.esper;

import java.util.HashMap;
import org.apache.log4j.Logger;
//import java.util.Iterator;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.bean.BeanEventBean;
import com.openwide.easysoa.esperpoc.NuxeoRegistrationService;
import com.openwide.easysoa.monitoring.Message;
import com.openwide.easysoa.monitoring.soa.Service;
import com.openwide.easysoa.monitoring.soa.WSDLService;


/**
 * Sync message listener
 * able to use all of Message's props, including content
 * 
 * @author jguillemotte
 *
 */
public class MessageListener implements UpdateListener {

	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(MessageListener.class.getName());
	
	/**
	 * 
	 */
	public void update(EventBean[] newDatas, EventBean[] oldData) {
		for (EventBean newData : newDatas) {
			update(newData);
		}
    }
	
	public void update(EventBean newData) {
		logger.debug("[MessageListener] --- Event received: " + newData.getUnderlying());
		logger.debug("[MessageListener] --- " + newData.getUnderlying().getClass().getName());
		NuxeoRegistrationService nrs = new NuxeoRegistrationService();
		@SuppressWarnings("unchecked")
		HashMap<String,Object> hm = (HashMap<String,Object>)(newData.getUnderlying());
		/*Iterator<String> iter = hm.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			System.out.println("Key : " + key);
			System.out.println("Value : " + hm.get(key));
			System.out.println("Clazz value : " + hm.get(key).getClass().getName());
		}*/
		BeanEventBean beb = (BeanEventBean)(hm.get("s"));
		Message msg = (Message)(beb.getUnderlying());
		
		// Service construction + send Esper event
		String serviceName = msg.getPathName();
		if(serviceName.startsWith("/")){
			serviceName = serviceName.substring(1);
		}
		serviceName = serviceName.replace('/', '_');
		
		if("WSDl".equals(msg.getType())){
			WSDLService service;
			service = new WSDLService(msg.getHost(), serviceName, msg.getCompleteMessage(), msg.getMethod());
			nrs.registerWSDLService(service);
			
		} else {
			// getting parent url
			int lastSlashIndex = msg.getUrl().lastIndexOf('/');
			String parentUrl = msg.getUrl().substring(0, lastSlashIndex);
			
			Service service = new Service(msg.getUrl(), parentUrl);
			// TODO also register to nuxeo parent apis if required
			nrs.registerRestService(service);
			// TODO put urlType in msg and handle it here to register also apis and appliimpls to nuxeo
		}
	}
	
}
