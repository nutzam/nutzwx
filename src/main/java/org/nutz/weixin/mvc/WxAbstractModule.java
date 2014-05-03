package org.nutz.weixin.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

public abstract class WxAbstractModule{
	
	protected WxHandler wxHandler;

	@At({"/weixin", "/weixin/?"})
	@Ok("wx")
	@Fail("http:200")
	public Object msgIn(String key, HttpServletRequest req) throws IOException {
		return Wxs.handle(getWxHandler(key), req);
	}
	
	public WxHandler getWxHandler(String key) {
		return wxHandler;
	}
}
