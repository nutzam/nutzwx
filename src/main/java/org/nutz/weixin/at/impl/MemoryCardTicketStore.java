package org.nutz.weixin.at.impl;

import org.nutz.weixin.at.WxCardTicket;
import org.nutz.weixin.spi.WxCardTicketStore;

/**
 * 
 * @author JinYi(wdhlzd@163.com)
 *
 */
public class MemoryCardTicketStore implements WxCardTicketStore {

    WxCardTicket ct;

    @Override
    public WxCardTicket get() {
        return ct;
    }

    @Override
    public void save(String ticket, int expires, long lastCacheTimeMillis) {
    	ct = new WxCardTicket(ticket, expires, lastCacheTimeMillis);
    }

}
