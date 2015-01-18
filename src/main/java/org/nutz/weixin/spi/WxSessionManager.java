package org.nutz.weixin.spi;

import org.nutz.weixin.bean.WxInMsg;

public interface WxSessionManager {

    WxSession getSession(String id);
    
    WxSession getSession(String id, boolean create);
    
    WxSession getSession(WxInMsg msg);
    
    WxSession getSession(WxInMsg msg, boolean create);
}
