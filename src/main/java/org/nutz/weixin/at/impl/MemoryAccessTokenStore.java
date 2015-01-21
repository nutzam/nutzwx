package org.nutz.weixin.at.impl;

import org.nutz.weixin.at.WxAccessToken;
import org.nutz.weixin.spi.WxAccessTokenStore;

public class MemoryAccessTokenStore implements WxAccessTokenStore {
	
	WxAccessToken at;

	public WxAccessToken getAccessToken() {
		return at;
	}

	public void saveAccessToken(String token, int time) {
		this.at = new WxAccessToken(token, time);
	}

}
