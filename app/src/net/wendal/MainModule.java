package net.wendal;

import javax.servlet.http.HttpSession;

import net.wendal.base.mvc.SmartViewMaker;

import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.mvc.view.ForwardView;
import org.nutz.mvc.view.JspView;

@Modules(scanPackage=true, packages={"net.wendal", "com.danoo"})
@IocBy(type=ComboIocProvider.class, args={"*js", "ioc/", "*annotation", "net.wendal", "com.danoo"})
@Ok("json:full")
@Fail("http:500")
@Localization("msg")
@SetupBy(value=MainSetup.class, args="ioc:mainSetup")
@Views(value=SmartViewMaker.class)
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
