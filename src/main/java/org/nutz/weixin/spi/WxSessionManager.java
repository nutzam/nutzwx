package org.nutz.weixin.spi;

import org.nutz.weixin.bean.WxInMsg;

public interface WxSessionManager {

	WxSession getSession(WxInMsg in);
	
}
