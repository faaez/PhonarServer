package com.phonarapp.server;

import java.util.List;

import org.apache.tools.ant.types.resources.Tokens;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class DataAccessLayer {
	
	public static final String AUTH_TOKEN_STRING = "DQAAAMMAAACL7u3MM621pp26GLzkRT9ELIXGxqPBOPRZrWezKkujGzKqit2ZkSun8jXxMVpUe4g2MDGT18RGuFaSFLIcdLkznm1Lw05tiDD7trm9dsNgOA7sPIsejdRiNv1EcZ_ZXKug5CuNYkQN0EbjtWomDyi-xBLWAYZo_8LKxaBm3xfLyMEYx3T2r41NlrnPQU5Wnc47_Q0O0IFkKhboayg6vNHKlVFtYJOIDcJblknuD0mL8lvQjCBQmKeop80LZuotkKEEKesjziOiPYphNVsj-cMg";
	
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
		Entity user = users.get(0);
		return (String) user.getProperty("registrationId");
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
	    //Entity token = tokens.get(0);
		return AUTH_TOKEN_STRING;
	}
}
