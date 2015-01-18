package org.nutz.weixin.session;

import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.spi.WxSession;
import org.nutz.weixin.spi.WxSessionManager;

public abstract class AbstractWxSessionManager implements WxSessionManager {

    public WxSession getSession(String id) {
        return getSession(id, true);
    }

    public WxSession getSession(WxInMsg msg) {
        return getSession(msg, true);
    }

    public WxSession getSession(WxInMsg msg, boolean create) {
        return getSession(msg.getFromUserName(), create);
    }


}
