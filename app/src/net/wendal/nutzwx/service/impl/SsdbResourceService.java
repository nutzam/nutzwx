package net.wendal.nutzwx.service.impl;

import net.wendal.nutzwx.service.ResourceService;

import org.nutz.json.Json;
import org.nutz.ssdb4j.spi.SSDB;

public class SsdbResourceService implements ResourceService {
	
	protected SSDB ssdb;

	public SsdbResourceService(String openid, SSDB ssdb) {
		super();
		this.ssdb = ssdb;
	}

	public boolean put(String openid, String key, String str) {
		ssdb.set(openid + "_" + key, str).check();
		return true;
	}

	public String get(String openid, String key) {
		return ssdb.get(openid + "_" + key).check().asString();
	}

	public <T> T getAsJsonObjet(String openid, String key, Class<T> klass) {
		return Json.fromJson(klass, get(openid, key));
	}

}
