package org.nutz.weixin.at.impl;

import org.nutz.weixin.at.WxJsapiTicket;
import org.nutz.weixin.spi.WxJsapiTicketStore;

public class MemoryJsapiTicketStore implements WxJsapiTicketStore {

	WxJsapiTicket jt;

	@Override
	public WxJsapiTicket get() {
		return jt;
	}

	@Override
	public void save(String ticket, int expires, long lastCacheTimeMillis) {
		jt = new WxJsapiTicket(ticket, expires, lastCacheTimeMillis);
	}

}
