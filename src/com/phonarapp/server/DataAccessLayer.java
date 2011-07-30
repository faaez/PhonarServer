package com.phonarapp.server;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DataAccessLayer {
	public static void registerUser(String number, String registrationId) {
		Key userKey = KeyFactory.createKey("User", number);
		Entity entity = new Entity(userKey);
		entity.setProperty("number", number);
		entity.setProperty("registrationId", registrationId);
		DatastoreServiceFactory.getDatastoreService().put(entity);
	}
}
