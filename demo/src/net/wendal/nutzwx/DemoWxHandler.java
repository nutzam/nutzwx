package net.wendal.nutzwx;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.util.Wxs;

@IocBean(name="wxHandler", args={"9b6b3a8ae"})
public class DemoWxHandler extends BasicWxHandler {

	public DemoWxHandler(String token) {
		super(token);
	}

	public WxOutMsg text(WxInMsg msg) {
		if ("god".equals(msg.getContent()))
			return Wxs.respText(null, "Oh my God!");
		else
			return Wxs.respText(null, "Out of my way!");
	}
}
