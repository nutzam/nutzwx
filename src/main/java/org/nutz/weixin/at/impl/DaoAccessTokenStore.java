package org.nutz.weixin.at.impl;

import java.util.Map;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.weixin.at.WxAccessToken;
import org.nutz.weixin.spi.WxAccessTokenStore;

public class DaoAccessTokenStore implements WxAccessTokenStore {

	protected Dao dao;
	protected Map<String, Object> params;

	protected String fetch = "select access_token,access_token_expires,access_token_lastat from wx_config where id=@id";
	protected String update = "update wx_config set access_token=@access_token, access_token_expires=@access_token_expires, access_token_lastat=@access_token_lastat where id=@id";

	public DaoAccessTokenStore() {
	}

	public DaoAccessTokenStore(Dao dao) {
		super();
		this.dao = dao;
	}

	@Override
	public WxAccessToken get() {
		Sql sql = Sqls.fetchRecord(fetch);
		if (params != null)
			sql.params().putAll(params);
		dao.execute(sql);
		Record record = sql.getObject(Record.class);
		WxAccessToken at = new WxAccessToken();
		at.setExpires(record.getInt("access_token_expires"));
		at.setLastCacheTimeMillis(record.getLong("access_token_lastat"));
		at.setToken(record.getString("access_token"));
		return at;
	}

	@Override
	public void save(String token, int time, long lastCacheTimeMillis) {
		Sql sql = Sqls.create(update);
		if (params != null)
			sql.params().putAll(params);
		sql.params().set("access_token", token);
		sql.params().set("access_token_expires", time);
		sql.params().set("access_token_lastat", lastCacheTimeMillis);
		dao.execute(sql);
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public void setFetch(String fetch) {
		this.fetch = fetch;
	}

	public void setUpdate(String update) {
		this.update = update;
	}
}
