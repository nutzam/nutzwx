package net.wendal.nutzwx.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;

/**
 * 平台用户
 *
 */
@Table("wx_admin_user")
public class AdminUser {

	@Name
	private String name;
	
	@ColDefine(width=256)
	@JsonField(ignore=true)
	private String passwd;
	@ColDefine(width=128)
	@JsonField(ignore=true)
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
