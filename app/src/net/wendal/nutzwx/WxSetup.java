package net.wendal.nutzwx;

import net.wendal.nutzwx.bean.AdminUser;
import net.wendal.nutzwx.bean.AdminUserDetail;
import net.wendal.nutzwx.util.Toolkit;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class WxSetup implements Setup {
	
	private static final Log log = Logs.get();
	
	protected Scheduler scheduler;

	@Override
	public void init(NutConfig nc) {
		Ioc ioc = nc.getIoc();
		Dao dao = ioc.get(Dao.class);
		PropertiesProxy pp = ioc.get(PropertiesProxy.class, "config");
		Daos.createTablesInPackage(dao, AdminUser.class.getPackage().getName(), false);
		if (dao.count(AdminUser.class) == 0) {
			AdminUser user = new AdminUser();
			String passwd = R.UU16();
			String slat = R.sg(48).next();
			user.setName("admin");
			user.setPasswd(Toolkit.passwordEncode(passwd, slat));
			user.setSlat(slat);
			dao.insert(user);
			log.warn("init admin user as passwd " + passwd);
		}
		if (dao.fetch(AdminUserDetail.class, "admin") == null) {
			AdminUserDetail detail = new AdminUserDetail();
			detail.setName("admin");
			detail.setEmail(pp.get("mail.from"));
			detail.setAlias("God");
			dao.insert(detail);
		}
		
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.startDelayed(5000);;
		} catch (SchedulerException e) {
			log.warn("Scheduler start fail", e);
		}

		for(String beanName: ioc.getNames()) {
			ioc.get(null, beanName);
		}
	}

	@Override
	public void destroy(NutConfig nc) {
		if (scheduler != null)
			try {
				scheduler.shutdown();
			} catch (SchedulerException e) {
				log.warn("Scheduler shutdown fail", e);
			}
	}

}
