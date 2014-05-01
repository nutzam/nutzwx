package org.nutz.weixin.spi;

import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;

public interface WxHandler {

	WxOutMsg text(WxInMsg msg);
	WxOutMsg image(WxInMsg msg);
	WxOutMsg voice(WxInMsg msg);
	WxOutMsg video(WxInMsg msg);
	WxOutMsg location(WxInMsg msg);
	WxOutMsg link(WxInMsg msg);
	//WxOutMsg event(WxInMsg msg);
	
	WxOutMsg eventSubscribe(WxInMsg msg);
	WxOutMsg eventScan(WxInMsg msg);
	WxOutMsg eventLocation(WxInMsg msg);
	WxOutMsg eventClick(WxInMsg msg);
	WxOutMsg eventView(WxInMsg msg);

	WxOutMsg defaultMsg(WxInMsg msg);
}
