package net.wendal.nutzwx.service;


public interface ResourceService {

	boolean put(String openid, String key, String str);
	
	String get(String openid, String key);
	
	<T> T getAsJsonObjet(String openid, String key, Class<T> klass);
}
