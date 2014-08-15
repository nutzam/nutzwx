package net.wendal.ito.mvc;

import net.wendal.ito.bean.ItoUser;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.VoidView;

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
			ItoUser itokey = dao.fetch(ItoUser.class, apikey);
			if (itokey != null) {
				ac.getRequest().setAttribute("userId", itokey.getUserId());
				return null;
			}
		}
		
		ac.getResponse().setStatus(403);
		return new VoidView();
	}
}
