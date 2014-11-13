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
