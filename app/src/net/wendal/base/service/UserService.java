package net.wendal.base.service;

import net.wendal.base.bean.User;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class UserService {

	@Inject Dao dao;
	
	public long userId(String name) {
		User user = dao.fetch(User.class, name);
		if (user == null)
			return -1;
		return user.getId();
	}
}
