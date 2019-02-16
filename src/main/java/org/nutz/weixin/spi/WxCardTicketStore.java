package org.nutz.weixin.spi;

import org.nutz.weixin.at.WxCardTicket;

public interface WxCardTicketStore {

	WxCardTicket get();

	void save(String ticket, int expires, long lastCacheTimeMillis);

}
