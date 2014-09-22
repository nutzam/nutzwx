package net.wendal.iot.mvc;

import net.wendal.iot.Iots;
import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.service.IotService;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

@IocBean
public class IotSetup implements Setup {
	
	private static final Log log = Logs.get();
	
	@Inject Dao dao;

	@Inject IotService iotService;
	
	@Override
	public void init(NutConfig nc) {
		String pkg = IotSensor.class.getPackage().toString();
		for (int i = 0; i < Iots.PART; i++) {
			Dao dao = Daos.ext(nc.getIoc().get(Dao.class), ""+i);
			Daos.createTablesInPackage(dao, pkg, false);
		}
		iotService.rootUser();
		log.info("iot setup complete");
	}
	
	@Override
	public void destroy(NutConfig nc) {
	}
}
