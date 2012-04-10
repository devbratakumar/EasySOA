/**
 * EasySOA Proxy
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

/**
 * 
 */
package org.easysoa.cxf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import org.apache.cxf.endpoint.Client;

/**
 * Meteo Mock test dispatcher.
 * Build and send the request to the provider
 * 
 * @author jguillemotte
 *
 */
public class MeteoMockDispatcher {

    /**
     * Build and send the test request
     * @param providerInFraSCAti Must be set to true if the provider is started inside FraSCAti, false otherwise
     * @return The provider response
     * @throws IOException, SOAPException If a problem occurs
     */
    public String sendRequest(boolean providerInFraSCAti) throws IOException, SOAPException{
        MessageFactory factory = MessageFactory.newInstance();
        //System.out.println(wsdlURL + "\n\n");
        String wsdlURL = null;
        
        QName serviceName;
        QName portName;
        InputStream is1;
        
        // To use with service generated by FraSCAti
        if(providerInFraSCAti){
            serviceName = new QName("http://ws.xml.javax/", "Provider");
            portName = new QName("http://ws.xml.javax/", "ProviderPort");
            wsdlURL = "http://localhost:8987/meteoMockProvider?wsdl";
            // Request to be used with FraSCAti. See issue 23. FraSCAti doesn't use JAX-WS annotations so service name, port name and Method name are different            
            is1 = Client.class.getResourceAsStream("/meteoMockRequestFraSCAti.xml");
        }
        // To use with service corresponding to JAX-WS annotations
        else {
            serviceName = new QName("http://org.easysoa.meteomock/", "MeteoMock");
            portName = new QName("http://org.easysoa.meteomock/", "MeteoMockPort");
            wsdlURL = "http://localhost:8988/meteoMockProvider?wsdl";
            // Original request
            is1 = Client.class.getResourceAsStream("/meteoMockRequest.xml");
        }
        
        Service service = Service.create(new URL(wsdlURL), serviceName);
        //Dispatch<DOMSource> dispatch = s.createDispatch(portName, DOMSource.class, Service.Mode.PAYLOAD);

        if (is1 == null) {
            System.err.println("Failed to create input stream from file " + "meteoMockRequest.xml, please check");
            return null;
        }
        SOAPMessage soapReq1 = factory.createMessage(new MimeHeaders(), is1);
        Dispatch<SOAPMessage> dispSOAPMsg = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);

        System.out.println("Invoking server through Dispatch interface using SOAPMessage");
        SOAPMessage soapResp = dispSOAPMsg.invoke(soapReq1);
        System.out.println("Response from server: " + soapResp.getSOAPBody().getTextContent());
        return soapResp.getSOAPBody().getTextContent();
    }
    
}