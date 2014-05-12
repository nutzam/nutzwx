package org.nutz.weixin.bean;

import java.util.List;

import org.nutz.json.JsonField;

public class WxMenu {

	private String name;
	private String type;
	private String key;
	private String url;
	@JsonField("sub_button")
	private List<WxMenu> subButtons;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<WxMenu> getSubButtons() {
		return subButtons;
	}
	public void setSubButtons(List<WxMenu> subButtons) {
		this.subButtons = subButtons;
	}
}
