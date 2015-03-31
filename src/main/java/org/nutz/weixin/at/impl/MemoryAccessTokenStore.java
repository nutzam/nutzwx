package org.nutz.weixin.at.impl;

import org.nutz.weixin.at.WxAccessToken;
import org.nutz.weixin.spi.WxAccessTokenStore;

public class MemoryAccessTokenStore implements WxAccessTokenStore {

    WxAccessToken at;

    public WxAccessToken get() {
        return at;
    }

    public void save(String token, int time) {
        at = new WxAccessToken();
        at.setToken(token);
        at.setExpires(time);
    }

}
