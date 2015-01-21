package org.nutz.weixin.spi;

import org.nutz.weixin.at.WxAccessToken;

public interface WxAccessTokenStore {

	WxAccessToken getAccessToken();
	
	void saveAccessToken(String token, int time);
}
