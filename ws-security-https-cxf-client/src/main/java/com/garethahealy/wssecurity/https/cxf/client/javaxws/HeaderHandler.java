/*
 * #%L
 * ws-security-https-cxf-client
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.wssecurity.https.cxf.client.javaxws;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

	/*
     * NOTE: This class is a barebones interaction with javax.xml.soap.
	 * 		 Its much better to use CXF to attach headers - see link belo!
	 * 		 Please dont go down this route!
	 * 
	 * 		 http://cxf.apache.org/faq.html#FAQ-HowcanIaddsoapheaderstotherequest/response?
	 */

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        try {
            SOAPMessage message = context.getMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader header = envelope.getHeader();

            SOAPFactory factory = SOAPFactory.newInstance();

            SOAPElement myheader = factory.createElement("MyHeader");
            SOAPElement name = factory.createElement("MyName");
            name.addTextNode("Gareth");

            myheader.addChildElement(name);
            header.addChildElement(myheader);

            message.saveChanges();

            message.writeTo(System.out);
        } catch (SOAPException | IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void close(MessageContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<QName> getHeaders() {
        // TODO Auto-generated method stub
        return new TreeSet<QName>();
    }

}
