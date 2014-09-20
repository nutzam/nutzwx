package net.wendal.iot.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wendal.base.bean.User;
import net.wendal.iot.bean.IotUser;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

@IocBean
@At("/iot/mqtt/auth")
public class MqttAuthModule {

	@Inject Dao dao;
	
	static View HTTP_403 = new View() {
		
		public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
			resp.setStatus(403);
		}
	};
	
	private static final Log log = Logs.get();
	
	@At("/user")
	@Ok("void")
	public View get_user(@Param("username")String username, @Param("password")String password) {
		log.infof("u=%s p=%s", username, password);;
		if (Strings.isBlank(username))
			return HTTP_403;
		User user = dao.fetch(User.class, username);
		if (user != null && dao.count(IotUser.class, Cnd.where("userId", "=", user.getId()).and("apikey", "=", password)) == 1)
			return null;
		return HTTP_403;
	}
	
	@At("/super")
	@Ok("void")
	public View isSuperUser(@Param("username")String username) {
		if ("root".equals(username))
			return null;
		return HTTP_403;
	}
	
	@At("/acl")
	@Ok("void")
	public View acl(@Param("username")String username, @Param("topic")String topic, @Param("acc")String acc) {
		return  null;
	}
}
