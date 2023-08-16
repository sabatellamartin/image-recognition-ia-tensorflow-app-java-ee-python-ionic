package com.cubit.celerity.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Reference 
 * 
 * https://blogs.oracle.com/enterprisetechtips/consuming-restful-web-services-with-the-jersey-client-api
 * 
 * The Client class is the main configuration point for 
 * building a RESTful web service client.
 * You use it to configure various client properties 
 * and features and indicate which resource providers to use. 
 * Creating an instance of a Client is an expensive 
 * operation, so try to avoid creating an unnecessary number 
 * of client instances. A good approach is to reuse an 
 * existing instance, when possible.
 * 
 */

@Startup
@Singleton
public class JerseyClient {

	private Client jerseyClient = null;
	
	@PostConstruct
	void init() {
		if (jerseyClient == null) {
			jerseyClient = Client.create(this.getClientConfig());
		}
	}
	
	public Client getJerseyClient() {
		return jerseyClient;
	}
	
	private ClientConfig getClientConfig() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		return clientConfig;
	}
  
}