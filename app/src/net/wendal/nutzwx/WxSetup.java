package net.wendal.nutzwx;

import net.wendal.nutzwx.service.ResourceService;
import net.wendal.nutzwx.service.impl.DaoResourceService;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.Ioc2;
import org.nutz.ioc.ObjectProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.weixin.bean.WxUser;
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
		Daos.createTablesInPackage(dao, WxUser.class.getPackage().toString(), false);
//		PropertiesProxy pp = ioc.get(PropertiesProxy.class, "config");
		
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
