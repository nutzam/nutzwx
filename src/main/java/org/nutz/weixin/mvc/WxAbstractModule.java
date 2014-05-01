package org.nutz.weixin.mvc;

import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.RawView;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

public class WxAbstractModule{
	
	protected WxHandler wxHandler;

	@AdaptBy(type = WxAdaptor.class)
	public Object msgIn(WxInMsg msg, @Param("echostr") String echostr) {
		if (echostr != null) {
			return new ViewWrapper(new RawView(null), echostr);
		}
		return Wxs.handle(msg, wxHandler);
	}
}
