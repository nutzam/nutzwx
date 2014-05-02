package org.nutz.weixin.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.view.HttpStatusView;
import org.nutz.mvc.view.RawView;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

public abstract class WxAbstractModule{
	
	protected WxHandler wxHandler;

	@At({"/weixin", "/weixin/?"})
	@Ok("wx")
	@Fail("http:200")
	public Object msgIn(String key, HttpServletRequest req) throws IOException {
		WxHandler wxHandler = getWxHandler(key);
		if (wxHandler == null) {
			return HttpStatusView.HTTP_502;
		}
		if ("GET".equalsIgnoreCase(req.getMethod())) {
			if (!wxHandler.check(req.getParameter("signature"), req.getParameter("timestamp"), req.getParameter("nonce"))) {
				return HttpStatusView.HTTP_502;
			}
			return new ViewWrapper(new RawView(null), req.getParameter("echostr"));
		}
		WxInMsg in = Wxs.convert(req.getInputStream());
		WxOutMsg out = Wxs.handle(in, wxHandler);
		if (out != null)
			Wxs.fix(in, out);
		return out;
	}
	
	public WxHandler getWxHandler(String key) {
		return wxHandler;
	}
}
