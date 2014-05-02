package net.wendal.nutzwx.module;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.weixin.mvc.WxAbstractModule;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

@IocBean
public class DemoWxModule extends WxAbstractModule {

	public DemoWxModule() {
		Wxs.enableDevMode();
	}
	
	@Inject
	private WxHandler wxHandler;
	
	public WxHandler getWxHandler(String key) {
		return wxHandler;
	}
	
	@At("/weixin")
	@Ok("wx")
	@Fail("http:200")
	@AdaptBy(type=VoidAdaptor.class)
	public Object msgIn(HttpServletRequest req) throws IOException {
		return super.msgIn(null, req);
	}

}
