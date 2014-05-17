package net.wendal.nutzwx.module;

import javax.servlet.http.HttpSession;

import net.wendal.nutzwx.bean.User;
import net.wendal.nutzwx.util.Toolkit;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

@IocBean
@At("/usr")
public class UserModule {
	
	private static final Log log = Logs.get();

	@Inject
	protected Dao dao;
	
	@At
	public boolean login(@Param("username")String name,
					  @Param("password")String password,
					  @Param("captcha")String captcha){
		if (name == null || password == null || captcha == null)
			return false;
		if (name.length() > 64 || password.length() > 64 || captcha.length() > 64)
			return false;
		HttpSession session = Mvcs.getHttpSession(false);
		if (session == null) { // 直接调用login,没门!
			return false;
		}
		Object _captcha = session.getAttribute(Toolkit.captcha_attr);
		if (_captcha == null) //连验证码都没有咯,没门!
			return false;
		if (!Toolkit.checkCaptcha(_captcha.toString(), captcha)) {
			Lang.sleep(1000); // 暂停1s再响应. TODO 容易导致DDOS
			return false; // 验证码不过? 没门!
		}
		
		// TODO 下面是检查登录信息的逻辑,应当抽象为一个接口实现.
		User usr = dao.fetch(User.class, Cnd.where("name", "=", name));
		if (usr == null || !Toolkit.passwordEncode(password, usr.getSlat()).equals(usr.getPasswd())) {
			Lang.sleep(1000); // 暂停1s再响应. TODO 容易导致DDOS
			return false;
		}
		
		log.debug("User login success >> " + name);
		// TODO 记录到系统操作日志中
		session.setAttribute("usr", name); // session里面只放用户名哦
		return true;
	}
	
	@At
	public void logout(){
		HttpSession session = Mvcs.getHttpSession(false);
		if (session != null)
			session.invalidate();
	}
	
	@At
	@Ok("jsp:usr.index")
	public void home() {
	}
	
}
