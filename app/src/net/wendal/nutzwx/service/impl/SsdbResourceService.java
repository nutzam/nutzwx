package net.wendal.nutzwx.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.nutz.json.Json;
import org.nutz.ssdb4j.spi.SSDB;

import net.wendal.nutzwx.service.ResourceService;

public class SsdbResourceService implements ResourceService {
	
	protected SSDB ssdb;

	public SsdbResourceService(SSDB ssdb) {
		super();
		this.ssdb = ssdb;
	}

	public boolean put(String key, InputStream obj) {
		ssdb.set(key, obj).check();
		return true;
	}

	public InputStream get(String key) {
		return new ByteArrayInputStream(ssdb.get(key).check().datas.get(0));
	}

	public <T> T getAsJsonObjet(String key, Class<T> klass) {
		return Json.fromJson(klass, new InputStreamReader(get(key)));
	}

}
