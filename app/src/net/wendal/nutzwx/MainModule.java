package net.wendal.nutzwx;

import javax.servlet.http.HttpSession;

import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.mvc.view.ForwardView;
import org.nutz.mvc.view.JspView;

@Modules(scanPackage=true)
@IocBy(type=ComboIocProvider.class, args={"*js", "ioc/", "*annotation", "net.wendal.nutzwx"})
@Ok("json:full")
@Fail("http:500")
@Localization("msg")
@SetupBy(WxSetup.class)
public class MainModule {
	
	protected static View usrLogin = new ForwardView("/login.jsp");
	protected static JspView USER_HOME_PAGE = new JspView("jsp.usr.index");
	
	@At("/home")
	public View index() {
		HttpSession session = Mvcs.getHttpSession();
		if (session.getAttribute("usr") == null)
			return usrLogin;
		return USER_HOME_PAGE;
	}
}
