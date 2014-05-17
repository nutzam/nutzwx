package org.nutz.weixin.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

public abstract class WxAbstractModule{
	
	protected WxHandler wxHandler;

	@At({"/weixin", "/weixin/?"})
	@Fail("http:200")
	public View msgIn(String key, HttpServletRequest req) throws IOException {
		return Wxs.handle(getWxHandler(key), req);
	}
	
	public WxHandler getWxHandler(String key) {
		return wxHandler;
	}
}
