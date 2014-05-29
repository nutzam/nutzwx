package net.wendal.nutzwx.service.impl;

import java.sql.Clob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialClob;

import net.wendal.nutzwx.bean.WxMsgStore;
import net.wendal.nutzwx.service.ResourceService;

import org.nutz.dao.Dao;
import org.nutz.dao.util.ExtDaos;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class DaoResourceService implements ResourceService {
	
	private static final Log log = Logs.get();
	
	protected Dao dao;
	public DaoResourceService(Dao dao) {
		this.dao = dao;
	}

	public boolean put(String openid, String key, String str) {
		try {
			Clob body = new SerialClob(str.toCharArray());
			ExtDaos.ext(dao, openid).insert(new WxMsgStore(key, body));
			return true;
		} catch (Exception e) {
			log.info("put fail", e);
		}
		return false;
	}

	public String get(String openid, String key) {
		WxMsgStore bean = ExtDaos.ext(dao, openid).fetch(WxMsgStore.class, key);
		if (bean != null)
			try {
				return Lang.readAll(bean.getBody().getCharacterStream());
			} catch (SQLException e) {
				log.info("fail", e);
			}
		return null;
	}
	
	@Override
	public <T> T getAsJsonObjet(String openid, String key, Class<T> klass) {
		String str = get(openid, key);
		if (str != null)
			return Json.fromJson(klass, str);
		return null;
	}
}
