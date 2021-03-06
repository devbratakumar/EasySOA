/**
 * EasySOA Registry
 * Copyright 2011 Open Wide
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact : easysoa-dev@googlegroups.com
 */

package org.easysoa.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easysoa.doctypes.AppliImpl;
import org.easysoa.doctypes.Service;
import org.easysoa.test.EasySOACoreTestFeature;
import org.easysoa.test.EasySOARepositoryInit;
import org.easysoa.test.StaticWebServer;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

/**
 * Tests notification service
 * @author mkalam-alami, mdutoo
 *
 */
@RunWith(FeaturesRunner.class)
@Features(EasySOACoreTestFeature.class)
@RepositoryConfig(init=EasySOARepositoryInit.class)
public class DocumentServiceTest {

    static final Log log = LogFactory.getLog(DocumentServiceTest.class);
    
    @Inject DocumentService docService;
    
    @Inject CoreSession session;

    private static StaticWebServer staticServer;
    
    @Before
    public void initTest() throws Exception {
  	  	assertNotNull("Cannot get document service component", docService);
    }
    
    @BeforeClass
    public static void setUp() throws Exception {
  	  	staticServer = new StaticWebServer(9010).start();
    }
    
    @AfterClass
    public static void tearDown() throws Exception {
    	staticServer.stop();
    }
    
    /**
     * Test document merge
     * @throws InterruptedException 
     * @throws Exception
     */
    @Test
    public void testMerge() throws ClientException, MalformedURLException, InterruptedException {

    	String url = "http://www.easysoa.com/services/coffee",
    		apiUrl = "http://www.easysoa.com/services",
    		lightUrl = "http://www.easysoa.com/web/coffee",
    		description = "Free coffee for everyone";

    	// Create API
    	DocumentModel appliImplModel = docService.getDefaultAppliImpl(session);
    	DocumentModel apiModel = docService.createServiceAPI(session, appliImplModel.getPathAsString(), apiUrl);
    	session.save();
    	
    	// Create services
    	DocumentModel model1 = docService.createService(session, apiModel.getPathAsString(), url);
    	model1.setProperty(Service.SCHEMA, Service.PROP_LIGHTURL, lightUrl);
    	session.saveDocument(model1);
    	session.save();
    	DocumentModel model2 = docService.createService(session, apiModel.getPathAsString(), url);
    	model2.setProperty(Service.SCHEMA_DUBLINCORE, Service.PROP_DESCRIPTION, description);
    	session.saveDocument(model2);
    	session.save();
    	
    	// Fetch service
    	DocumentModelList list = session.query("SELECT * FROM Service");
    	assertEquals(1, list.size());
    	DocumentModel mergedModel = list.get(0);
    	assertEquals(lightUrl, mergedModel.getProperty(Service.SCHEMA, Service.PROP_LIGHTURL));
    	assertEquals(description, mergedModel.getProperty(Service.SCHEMA_DUBLINCORE, Service.PROP_DESCRIPTION));
    	
    }
    
    /**
     * Test the file download and parsing when the URL matches a WSDL
     * @throws InterruptedException 
     * @throws Exception
     */
    @Test @Ignore
    public void testWSDL() throws ClientException, MalformedURLException, InterruptedException {

    	String wsdlUrl = "http://localhost:9010/PureAirFlowers.wsdl",
    		apiUrl = "http://localhost:9010",
    		serviceUrl = "http://localhost:9010/PureAirFlowers";
    	
    	// Create API
    	DocumentModel appliImplModel = docService.getDefaultAppliImpl(session);
    	DocumentModel apiModel = docService.createServiceAPI(session, appliImplModel.getPathAsString(), apiUrl);

    	// Create Service with WSDL adress only
    	DocumentModel model = docService.createService(session, apiModel.getPathAsString(), serviceUrl);
    	model.setProperty(Service.SCHEMA, Service.PROP_FILEURL, wsdlUrl);
    	session.saveDocument(model);
    	session.save();
    	
    	// Check service contents (XXX: WSDL download is asynchronous, test cannot pass)
    	model = docService.findService(session, serviceUrl);
    	assertNotNull("Failed to fetch document by url", model);
    	assertEquals("Failed to extract data from WSDL", "http://www.ebi.ac.uk/WSCensor", 
    			model.getProperty(Service.SCHEMA, Service.PROP_WSDLNAMESPACE));
    	assertNotNull("Failed to save WSDL in 'file' schema", model.getProperty("file", "content"));
    	    	
    }
    
    /**
     * Test the service fetch by its fileUrl value
     * @throws Exception
     */
    @Test
    public void testFindByWSDL() throws ClientException, MalformedURLException {

    	String wsdlUrl = "http://soatest.parasoft.com/calculator.wsdl",
    		apiUrl = "http://soatest.parasoft.com/glue",
    		serviceUrl = "http://ws1.parasoft.com/glue/calculator";

    	// Create API (if not done by previous test)
    	DocumentModel apiModel = docService.findServiceApi(session, apiUrl);
    	if (apiModel == null) {
        	DocumentModel appliImplModel = docService.getDefaultAppliImpl(session);
    		apiModel = docService.createServiceAPI(session, appliImplModel.getPathAsString(), apiUrl);
    	}

    	// Create Service with WSDL adress only
    	DocumentModel model = docService.createService(session, apiModel.getPathAsString(), serviceUrl);
    	model.setProperty(Service.SCHEMA, Service.PROP_FILEURL, wsdlUrl);
    	session.saveDocument(model);
    	session.save();
    	
    	// Find Service
    	model = docService.findService(session, wsdlUrl);
    	assertNotNull("Failed to fetch document by fileUrl", model);
    	
    }

    /**
     * Test that the default application is rebuilt if deleted
     * @throws Exception
     */
    @Test
    public void testDefaultAppRegen() throws ClientException, MalformedURLException {
    	
    	// Remove default appli impl.
    	DocumentModel appliImplModel = docService.findAppliImpl(session, AppliImpl.DEFAULT_APPLIIMPL_URL);
    	session.removeDocument(appliImplModel.getRef());
    	Assume.assumeTrue(docService.findAppliImpl(session, AppliImpl.DEFAULT_APPLIIMPL_URL) == null);
    	
    	// Create random API
    	docService.createServiceAPI(session, null, "http://www.random-api.com");
    	appliImplModel = docService.findAppliImpl(session, AppliImpl.DEFAULT_APPLIIMPL_URL);
    	assertNotNull(appliImplModel);
    	assertEquals(appliImplModel.getTitle(), AppliImpl.DEFAULT_APPLIIMPL_TITLE);
    }
    
}