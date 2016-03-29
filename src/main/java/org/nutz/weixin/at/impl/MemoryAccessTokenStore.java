package org.nutz.weixin.at.impl;

import org.nutz.weixin.at.WxAccessToken;
import org.nutz.weixin.spi.WxAccessTokenStore;

public class MemoryAccessTokenStore implements WxAccessTokenStore {

	WxAccessToken at;

	@Override
	public WxAccessToken get() {
		return at;
	}

	@Override
	public void save(String token, int time, long lastCacheTimeMillis) {
		at = new WxAccessToken();
		at.setToken(token);
		at.setExpires(time);
		at.setLastCacheTimeMillis(lastCacheTimeMillis);
	}

}
