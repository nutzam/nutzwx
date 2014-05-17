package net.wendal.nutzwx.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 平台用户
 *
 */
@Table("wx_admin_user")
public class User {

	@Name
	private String name;
	
	@ColDefine(width=256)
	private String passwd;
	@ColDefine(width=128)
	private String slat;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getSlat() {
		return slat;
	}
	public void setSlat(String slat) {
		this.slat = slat;
	}
}
