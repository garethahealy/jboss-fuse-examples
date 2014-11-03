package com.garethahealy.wssecurity.https.cxf.client;

import com.garethahealy.wssecurity.https.cxf.client.impl.EndpointService;

public class Client {

	public static void main(String [] args)
	{
		EndpointService service = new EndpointService();
		service.sayHello();
	}
}
