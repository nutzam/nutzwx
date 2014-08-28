package net.wendal.base.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.util.NutMap;
import org.nutz.mvc.View;
import org.nutz.mvc.view.HttpStatusView;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.UTF8JsonView;

public class SmartView implements View {
	
	public static boolean DEBUG_MODE = false;
	
	private String val;
	
	public SmartView(String val) {
		this.val = val;
	}
	
	protected View jsonView() {
		if (DEBUG_MODE)
			return UTF8JsonView.FULL;
		return UTF8JsonView.COMPACT;
	}

	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
		String uri = req.getRequestURI();
		if (uri.endsWith("/") || uri.endsWith(".")) {
			HttpStatusView.HTTP_500.render(req, resp, obj);
			return;
		}
		String reqName = uri.substring(uri.lastIndexOf('/'));
		if (!reqName.contains(".")) {
			jsonView().render(req, resp, obj);
			return;
		}
		String suffix = reqName.substring(reqName.lastIndexOf('.'));
		if ("ajax".equals(suffix)) {
			jsonView().render(req, resp, new NutMap().setv("ok", "true").setv("data", obj));
		} else if ("xml".equals(suffix)) {
			HttpStatusView.HTTP_502.render(req, resp, obj);
		} else if ("htm".equals(suffix)) {
			new JspView(val).render(req, resp, obj);
		} else {
			jsonView().render(req, resp, obj);
		}
	}

}
