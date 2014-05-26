package net.wendal.nutzwx.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import net.wendal.nutzwx.service.ResourceService;

import org.nutz.dao.Dao;
import org.nutz.json.Json;
import org.nutz.lang.Streams;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class DaoResourceService implements ResourceService {
	
	private static final Log log = Logs.get();
	
	protected Dao dao;
	public DaoResourceService(Dao dao) {
		this.dao = dao;
	}

	public boolean put(String key, InputStream obj) {
		try {
			Blob blob = new SerialBlob(Streams.readBytesAndClose(obj));
			dao.insert(new WxResourceBean(key, blob));
			return true;
		} catch (Exception e) {
			log.info("put fail", e);
		}
		return false;
	}

	public InputStream get(String key) {
		WxResourceBean bean = dao.fetch(WxResourceBean.class, key);
		if (bean != null)
			try {
				return bean.getBody().getBinaryStream();
			} catch (SQLException e) {
				log.info("fail", e);
			}
		return null;
	}
	
	@Override
	public <T> T getAsJsonObjet(String key, Class<T> klass) {
		InputStream in = get(key);
		if (in != null)
			return Json.fromJson(klass, new InputStreamReader(in));
		return null;
	}
}
