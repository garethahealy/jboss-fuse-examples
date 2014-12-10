package com.garethahealy.wssecurity.https.cxf.client.resolvers;

public interface Resolver<T> {

    T createEndpoint();
}
