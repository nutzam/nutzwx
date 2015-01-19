package org.nutz.weixin.spi;

import org.nutz.weixin.bean.WxInMsg;

/**
 *  微信会话管理器
 *  @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxSessionManager {

    WxSession getSession(String id);
    
    WxSession getSession(String id, boolean create);
    
    WxSession getSession(WxInMsg msg);
    
    WxSession getSession(WxInMsg msg, boolean create);
}
