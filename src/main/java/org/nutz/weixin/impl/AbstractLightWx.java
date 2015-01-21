package org.nutz.weixin.impl;

import org.nutz.weixin.session.memory.MemorySessionManager;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxSessionManager;

public abstract class AbstractLightWx extends AbstractWxHandler {

    protected WxApi2 api;
    
    protected WxSessionManager sessionManager;
    
    public AbstractLightWx() {
        api = new WxApi2Impl();
        sessionManager = new MemorySessionManager();
    }
    
}
