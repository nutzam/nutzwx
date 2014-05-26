package net.wendal.nutzwx.service;

import java.io.InputStream;

public interface ResourceService {

	boolean put(String key, InputStream obj);
	
	InputStream get(String key);
	
	<T> T getAsJsonObjet(String key, Class<T> klass);
}
