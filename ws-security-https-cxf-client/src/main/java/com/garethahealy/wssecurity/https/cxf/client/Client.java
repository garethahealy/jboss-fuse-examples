package com.garethahealy.wssecurity.https.cxf.client;

import com.garethahealy.wssecurity.https.cxf.client.impl.WsHelloWorldService;

public class Client {

	public static void main(String [] args)
	{
		WsHelloWorldService service = new WsHelloWorldService();
		service.sayHello();
	}
}
