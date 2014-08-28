package net.wendal.iot.mvc;

import net.wendal.Zs;
import net.wendal.iot.bean.IotUser;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.HttpStatusView;

@IocBean
public class ApiKeyFilter implements ActionFilter {
	
	@Inject
	protected Dao dao;

	public View match(ActionContext ac) {
		String apikey = ac.getRequest().getHeader("U-ApiKey");
		if (apikey == null) {
			apikey = ac.getRequest().getHeader("ApiKey");
		}
		if (apikey != null) {
			IotUser itokey = dao.fetch(IotUser.class, apikey);
			if (itokey != null) {
				ac.getRequest().setAttribute(Zs.UID, itokey.getUserId());
				return null;
			}
		}
		if ("GET".equals(ac.getRequest().getMethod()) && !ac.getRequest().getRequestURI().endsWith("devices"))
			return null;

		return new HttpStatusView(403);
	}
}
