package org.nutz.weixin.spi;

import org.nutz.weixin.at.WxJsapiTicket;

public interface WxJsapiTicketStore {

	WxJsapiTicket get();

	void save(String ticket, int expires, long lastCacheTimeMillis);

}
