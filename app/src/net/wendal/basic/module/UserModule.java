package net.wendal.basic.module;

import javax.servlet.http.HttpSession;

import net.wendal.basic.bean.User;
import net.wendal.basic.util.Toolkit;
import net.wendal.nutzwx.service.MailService;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.Context;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.Scope;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.ServerRedirectView;

@IocBean(create="init")
@At("/usr")
public class UserModule {
	
	private static final Log log = Logs.get();

	@Inject
	protected Dao dao;
	
	@Inject
	protected MailService mailService;
	
	@Inject
	protected PropertiesProxy config;
	
	@At
	public boolean login(@Param("name")String name,
					  @Param("passwd")String password,
					  @Param("captcha")String captcha,
					  @Attr(value="usr", scope=Scope.SESSION)String _usr){
		if (_usr != null) {
			return true;
		}
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
		session.setAttribute("usr", usr.getId()); // session里面只放id哦
		return true;
	}
	
	protected Object loginFail() {
		boolean isAjax = Mvcs.getReq().getHeader("") != null;
		if (isAjax)
			return true;
		return new ServerRedirectView("/");
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
	
	@At("/pwd_reset")
	public boolean requirePasswdReset(@Param("name")String name,
			  @Param("email")String email,
			  @Param("captcha")String captcha) {
		if (name == null || email == null || captcha == null)
			return false;
		if (name.length() > 64 || email.length() > 64 || captcha.length() > 64)
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
		User usr = dao.fetch(User.class, name);
		if (usr == null || !email.equalsIgnoreCase(usr.getEmail())) {
			log.debug("email not match");
			return false;
		}
		String tpl = "URL: ${url}/callback/${token}";
		Context ctx = Lang.context();
		ctx.set("url", Mvcs.getReq().getRequestURL().toString());
		String token = String.format("u=%s,t=%d,slat=%s", name, System.currentTimeMillis(), usr.getSlat());
		token = Toolkit._3DES_encode(pwdResetKey, token.getBytes());
		if (token == null)
			return false;
		ctx.set("token", token);
		return mailService.send(usr.getEmail(), "Password reset", tpl, ctx);
	}
	
	@At("/pwd_reset/callback/?")
	@Ok("jsp:jsp.usr.pwdreset.done")
	public boolean passwdResetCallback(String token) {
		if (Strings.isBlank(token)) {
			throw new IllegalArgumentException("bad token=["+token+"]");
		}
		String re = Toolkit._3DES_decode(pwdResetKey, Toolkit.hexstr2bytearray(token));
		if (re == null) {
			throw new IllegalArgumentException("bad token=["+token+"]");
		}
		NutMap map = Toolkit.kv2map(re);
		if (map.getLong("t") - 30*60*1000 > System.currentTimeMillis()) {
			throw new IllegalArgumentException("bad token=["+token+"] timeout");
		}
		String name = map.getString("u");
		User usr = dao.fetch(User.class, name);
		if (usr == null) {
			throw new IllegalArgumentException("bad token=["+token+"]");
		}
		if (!usr.getSlat().equals(map.getString("slat"))) {
			throw new IllegalArgumentException("bad token=["+token+"]");
		}
		// 看来都是正确的,那允许重置密码了
		String pwd = Toolkit.randomPasswd(usr);
		dao.update(usr, "(slat|passwd)"); // 重置slat,这样token就是一次性咯
		// TODO 记入系统操作日志
		log.info("AdminUser Password reset success >> " + name);
		return mailService.send(usr.getEmail(), "New Password", "Password: ${pwd}", Lang.context().set("pwd", pwd));
	}
	
	protected byte[] pwdResetKey;
	
	public void init() {
		pwdResetKey = R.sg(24).next().getBytes();
	}
}
