package net.wendal.ito.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("ito_user")
public class ItoUser {

	@Id
	@Column("uid")
	private long userId;
	
	@Name
	private String apikey;

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}

	public String getApikey() {
		return apikey;
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
	
}
