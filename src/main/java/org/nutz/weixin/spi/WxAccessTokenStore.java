package org.nutz.weixin.spi;

import org.nutz.weixin.at.WxAccessToken;

public interface WxAccessTokenStore {

	WxAccessToken get();

	void save(String token, int expires, long lastCacheTimeMillis);
}
