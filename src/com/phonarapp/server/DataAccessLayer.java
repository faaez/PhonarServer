package com.phonarapp.server;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class DataAccessLayer {
		
	public static void registerUser(String number, String registrationId) {
		Key userKey = KeyFactory.createKey("User", number);
		Entity entity = new Entity(userKey);
		entity.setProperty("number", number);
		entity.setProperty("registrationId", registrationId);
		DatastoreServiceFactory.getDatastoreService().put(entity);
	}
	
	public static String getUserRegId(String number) {
		Key userKey = KeyFactory.createKey("User", number);
		Query query = new Query("User", userKey);
		DatastoreService datastore 
			= DatastoreServiceFactory.getDatastoreService();
		List<Entity> users = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(1));
		if (users.size() > 0) {
			Entity user = users.get(0);
			return (String) user.getProperty("registrationId");
		} else {
			return null;
		}
	}
	
	public static void storeAuthToken(String token) {
		Key tokenKey = KeyFactory.createKey("AuthToken", "AuthToken");
		Entity entity = new Entity(tokenKey);
		entity.setProperty("token", token);
		DatastoreServiceFactory.getDatastoreService().put(entity);
	}
	
	public static String getAuthToken() {

		Key tokenKey = KeyFactory.createKey("AuthToken", "AuthToken");
	    Query query = new Query("AuthToken", tokenKey);
	    DatastoreService datastore 
	    	= DatastoreServiceFactory.getDatastoreService();
	    List<Entity> tokens = datastore.prepare(query).asList(
	    		FetchOptions.Builder.withLimit(1));
	    if (tokens.size() > 0) {
	    	Entity token = tokens.get(0);
	    	return (String) token.getProperty("token");
	    } else {
	    	return C2DMPusher.AUTH_TOKEN_STRING;
	    }
	}
}
