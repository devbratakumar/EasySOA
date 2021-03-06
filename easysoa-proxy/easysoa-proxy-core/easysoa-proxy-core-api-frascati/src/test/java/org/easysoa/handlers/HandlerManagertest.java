package org.easysoa.handlers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.easysoa.message.InMessage;
import org.easysoa.message.OutMessage;
import org.easysoa.proxy.core.api.exchangehandler.HandlerManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.ow2.frascati.FraSCAti;
import org.ow2.frascati.assembly.factory.processor.ProcessingContextImpl;
import org.ow2.frascati.util.FrascatiException;


public class HandlerManagertest {


    // Logger
    private static Logger logger = Logger.getLogger(HandlerManagertest.class.getName());

    /** The FraSCAti platform */
    protected static FraSCAti frascati;

    protected static ArrayList<Component> componentList;
    
    static {
        System.setProperty("org.ow2.frascati.bootstrap", "org.ow2.frascati.bootstrap.FraSCAti");
    }
    
    /**
     * Start FraSCAti
     * @throws FrascatiException 
     */
    protected static void startFraSCAti() throws FrascatiException {
        logger.info("FraSCATI Starting");
        componentList =  new ArrayList<Component>();
        frascati = FraSCAti.newFraSCAti();
    }
    
    /**
     * 
     * @throws FrascatiException
     */
    protected static void stopFraSCAti() throws FrascatiException{
        logger.info("FraSCATI Stopping");
        if(componentList != null){
            for(Component component : componentList){
                frascati.close(component);
            }
        }
    }
    
    
    @BeforeClass
    public static void setUp() throws Exception {
        // Start fraSCAti
        startFraSCAti();
        // Start handlerManager
        componentList.add(frascati.processComposite("handlerManager", new ProcessingContextImpl()));
    }
    
    @Test
    public void test() throws Exception {
        Component comp = frascati.getComposite("handlerManager/handlerManagerServiceBaseComp");
        HandlerManager var =  frascati.getService(comp, "handlerManagerService", HandlerManager.class);
        
        InMessage inMessage = new InMessage();
        inMessage.setProtocol("http");
        inMessage.setMethod("get");
        inMessage.setPath("openwide/easysoanews/");
        inMessage.setServer("www.easysoa.org");
        
        OutMessage outMessage = new OutMessage();
        var.handleMessage(inMessage, outMessage);
    }

}
