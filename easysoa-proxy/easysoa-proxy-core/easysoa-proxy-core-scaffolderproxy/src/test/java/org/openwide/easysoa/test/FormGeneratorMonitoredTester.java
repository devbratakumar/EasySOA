package org.openwide.easysoa.test;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ow2.frascati.assembly.factory.processor.ProcessingContextImpl;
import org.ow2.frascati.util.FrascatiException;

public class FormGeneratorMonitoredTester extends AbstractTest {

	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(FormGeneratorMonitoredTester.class.getClass());
    
	/**
	 * Init the remote systems for the test
	 * Frascati and HTTP Proxy
	 * Instantiate FraSCAti and retrieve services.
	 * @throws InterruptedException
	 */
	@BeforeClass
	public static void setUp() throws FrascatiException, InterruptedException {
		// Start fraSCAti
		startFraSCAti();
		// Start the soap service mock
		startSoapServiceMockComposite();
		// Start Scaffolding Proxy test
		//startScaffoldingProxyComposite();
		componentList.add(frascati.processComposite("src/main/resources/scaffoldingProxy_monitored.composite", new ProcessingContextImpl()));
	}
	
	/**
	 * Wait for an user action to stop the test 
	 * @throws ClientException
	 * @throws SOAPException
	 * @throws IOException
	 */
	@Test
	public final void testWaitUntilRead() throws Exception{
		logger.info("Scaffolding proxy test started, wait for user action to stop !");
		// Just push a key in the console window to stop the test
		System.in.read();
		logger.info("Scaffolding proxy test stopped !");
	}
	
}