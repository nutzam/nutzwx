package org.nutz.weixin.spi;

public interface WxAccessTokenApi {

    void setAccessTokenStore(WxAccessTokenStore ats);

    WxAccessTokenStore getAccessTokenStore();

    
    String getAccessToken();
}
