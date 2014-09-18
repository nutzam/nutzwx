package net.wendal.iot.mvc;

import java.util.List;

import net.wendal.base.bean.User;
import net.wendal.iot.Iots;
import net.wendal.iot.bean.IotDevice;
import net.wendal.iot.bean.IotLocation;
import net.wendal.iot.bean.IotUser;
import net.wendal.iot.bean.IotUserLevel;
import net.wendal.iot.service.IotService;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

@IocBean
public class IotSetup implements Setup {
	
	private static final Log log = Logs.get();
	
	@Inject Dao dao;

	@Inject IotService iotService;
	
	@Override
	public void init(NutConfig nc) {
		List<Class<?>> ks = Scans.me().scanPackage(IotUser.class);
		for (int i = 0; i < Iots.PART; i++) {
			Dao dao = Daos.ext(nc.getIoc().get(Dao.class), ""+i);
			for (Class<?> klass : ks) {
				if (klass.getAnnotation(Table.class) != null)
					dao.create(klass, false);
			}
		}
		if (dao.count(IotUser.class) == 0) {
			iotService.addUser(dao.fetch(User.class).getId(), IotUserLevel.SSVIP);
		}
	}
	
	@Override
	public void destroy(NutConfig nc) {
	}
}
