package net.wendal.nutzwx.bean;

import net.wendal.nutzwx.annotation.SimpleCURD;

import org.nutz.dao.entity.annotation.Table;
import org.nutz.weixin.bean.WxMaster;

@Table("wx_mp_info")
@SimpleCURD(cnd={"owner", "=", "${usr}"})
public class WxMpInfo extends WxMaster {

	private String owner;
	
	private String disable;
	
	private String handlerClass;

	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}
	
}
