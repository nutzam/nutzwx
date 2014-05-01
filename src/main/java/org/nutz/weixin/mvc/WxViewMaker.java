package org.nutz.weixin.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class WxViewMaker implements ViewMaker {

	private static final Log log = Logs.get();
	
	public View make(Ioc ioc, String type, String value) {
		if (!"wx".equals(type))
			return null;
		return new View() {
			public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
				if (obj == null) {
					log.debug("NULL resp...");
					return;
				}
			}
		};
	}

}
