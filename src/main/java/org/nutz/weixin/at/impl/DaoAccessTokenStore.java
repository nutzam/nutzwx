package org.nutz.weixin.at.impl;

import java.util.Map;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.weixin.at.WxAccessToken;

public class DaoAccessTokenStore extends CacheableAccessTokenStore {

	protected Dao dao;
	protected Map<String, Object> params;
	
	protected String fetch = "select access_token from t_wx_at";
	protected String update = "update t_wx_at set access_token=@token, access_token_expires=@access_token_expires";
	
	public DaoAccessTokenStore() {
	}
	
	public DaoAccessTokenStore(Dao dao) {
		super();
		this.dao = dao;
	}

	public WxAccessToken _getAccessToken() {
		Sql sql = Sqls.fetchRecord(fetch);
		if (params != null)
			sql.params().putAll(params);
		dao.execute(sql);
		WxAccessToken tmp = sql.getObject(Record.class).toPojo(WxAccessToken.class);
		return tmp;
	}

	public void _saveAccessToken(String token, int time) {
		Sql sql = Sqls.create(update);
		if (params != null)
			sql.params().putAll(params);
		sql.params().set("token", token);
		sql.params().set("access_token_expires", time);
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
