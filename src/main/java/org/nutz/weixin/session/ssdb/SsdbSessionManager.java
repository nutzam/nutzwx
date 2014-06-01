package org.nutz.weixin.session.ssdb;

import org.nutz.ssdb4j.spi.SSDB;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.spi.WxSession;
import org.nutz.weixin.spi.WxSessionManager;

public class SsdbSessionManager implements WxSessionManager {

	protected SSDB ssdb;
	
	protected String prefix;
	
	public SsdbSessionManager(SSDB ssdb, String prefix) {
		super();
		this.ssdb = ssdb;
		this.prefix = prefix;
	}

	public WxSession getSession(WxInMsg in) {
		String id = prefix+"_"+in.getFromUserName();
		if (ssdb.hget(id, "ctime").notFound())
			ssdb.hset(id, "ctime", System.currentTimeMillis());
		ssdb.hset(id, "latime", System.currentTimeMillis());
		return new SsdbWxSession(ssdb, id);
	}

}
