package net.wendal.nutzwx;

import net.wendal.base.bean.User;
import net.wendal.base.util.Toolkit;
import net.wendal.nutzwx.service.ResourceService;
import net.wendal.nutzwx.service.impl.DaoResourceService;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.Ioc2;
import org.nutz.ioc.ObjectProxy;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

@IocBean
public class WxSetup implements Setup {
	
	private static final Log log = Logs.get();
	
	@Inject(optional=true)
	protected Scheduler scheduler;

	@Override
	public void init(NutConfig nc) {
		Ioc ioc = nc.getIoc();
		Dao dao = ioc.get(Dao.class);
		PropertiesProxy pp = ioc.get(PropertiesProxy.class, "config");
		Daos.createTablesInPackage(dao, getClass().getPackage().getName(), false);
		if (dao.count(User.class) == 0) {
			User user = new User();
			String passwd = R.UU16();
			String slat = R.sg(48).next();
			user.setEmail("");
			user.setPasswd(Toolkit.passwordEncode(passwd, slat));
			user.setSlat(slat);

			user.setName("admin");
			user.setEmail(pp.get("mail.from"));
			user.setAlias("God");
			
			dao.insert(user);
			log.warn("init admin user as passwd " + passwd);
		}
		
		// 按需选择
		ResourceService resourceService = null;
//		try {
//			SSDB ssdb = SSDBs.pool("127.0.0.1", 8888, 5000, null);
//			resourceService = new SsdbResourceService(ssdb);
//			((Ioc2)ioc).getIocContext().save("app", "resourceService", new ObjectProxy(resourceService));
//		} catch (Exception e) {
//			log.info("fail to connect ssdb? using DaoResourceService now", e);
			resourceService = new DaoResourceService(dao);
			((Ioc2)ioc).getIocContext().save("app", "resourceService", new ObjectProxy(resourceService));
//		}
		
//		try {
//			scheduler = StdSchedulerFactory.getDefaultScheduler();
//			scheduler.startDelayed(5000);;
//		} catch (SchedulerException e) {
//			log.warn("Scheduler start fail", e);
//		}


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
