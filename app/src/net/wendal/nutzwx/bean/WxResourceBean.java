package net.wendal.nutzwx.bean;

import java.sql.Blob;

import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("wx_resource")
public class WxResourceBean {

	@Name
	private String name;
	private Blob body;
	public WxResourceBean() {
		// TODO Auto-generated constructor stub
	}
	
	public WxResourceBean(String name, Blob body) {
		super();
		this.name = name;
		this.body = body;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Blob getBody() {
		return body;
	}
	public void setBody(Blob body) {
		this.body = body;
	}
	
	
}
