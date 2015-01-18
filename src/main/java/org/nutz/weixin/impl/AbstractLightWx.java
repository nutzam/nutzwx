package org.nutz.weixin.impl;

import org.nutz.weixin.session.memory.MemorySessionManager;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxSessionManager;

public abstract class AbstractLightWx extends BasicWxHandler {

    protected WxApi2 api;
    
    protected WxSessionManager sessionManager;
    
    public AbstractLightWx() {
        final AbstractLightWx self = this;
        api = new WxApi2Impl() {
            public void saveAccessToken(String token, long timeout) {
                self.saveAccessToken(token, timeout);
            }
        };
        sessionManager = new MemorySessionManager();
    }
    
    protected void saveAccessToken(String token, long timeout) {
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public void setAppid(String appid) {
        ((WxApi2Impl)api).appid = appid;
    }
    
    public void setAppSecret(String appsecret) {
        ((WxApi2Impl)api).appsecret = appsecret;
    }
    
    public void setAppOpenid(String openid) {
        ((WxApi2Impl)api).openid = openid;
    }
}
