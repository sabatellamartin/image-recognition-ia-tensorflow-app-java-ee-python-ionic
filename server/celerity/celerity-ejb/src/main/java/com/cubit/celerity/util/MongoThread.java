package com.cubit.celerity.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * http://mongodb.github.io/mongo-java-driver/2.13/getting-started/quick-tour/#making-a-connection:b8bcd3c4cba9ac16433f82561ee44461
 */

@Startup
@Singleton
public class MongoThread {

	private MongoClient mongoClient = null;
	
	private String host = Constants.MONGODBHOST.toString();
	
	private Integer port = Integer.parseInt(Constants.MONGODBPORT);
		
	@PostConstruct
	void init() {
		if (mongoClient == null) {
			mongoClient = new MongoClient(host, port);
		}
	}
	
	public MongoClient getMongoClient() {
		return mongoClient;
	}
	
	public MongoDatabase getDB() {
		return mongoClient.getDatabase(Constants.MONGODBNAME);
	}
  
}