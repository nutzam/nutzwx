package org.nutz.weixin.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.util.Wxs;

public class WxViewMaker implements ViewMaker {

	public static final WxView V = new WxView();
	
	public View make(Ioc ioc, String type, String value) {
		if (!"wx".equals(type))
			return null;
		return V;
	}

}

class WxView implements View {
	
	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
		if (obj == null) {
			return;
		}
		Wxs.asXml(resp.getWriter(), (WxOutMsg) obj);
	}
}
