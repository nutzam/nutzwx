package net.wendal.nutzwx.bean;

import java.sql.Clob;

import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("wx_resource_${openid}")
public class WxMsgStore {

	@Name
	private String name;
	public WxMsgStore(String name, Clob body) {
		super();
		this.name = name;
		this.body = body;
	}
	private Clob body;
	public WxMsgStore() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Clob getBody() {
		return body;
	}
	public void setBody(Clob body) {
		this.body = body;
	}
	
	
}
