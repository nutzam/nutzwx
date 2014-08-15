package net.wendal;

import java.util.List;

import net.wendal.basic.bean.User;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

public class MainSetup implements Setup {

	public void init(NutConfig nc) {
		Ioc ioc = nc.getIoc();
		Dao dao = ioc.get(Dao.class);
		Daos.createTablesInPackage(dao, User.class.getPackage().getName(), false);
		List<Class<?>> list = Scans.me().scanPackage(getClass(), "^.+Setup.class$");
		for (Class<?> klass : list) {
			if (klass == getClass())
				continue;
			((Setup) ioc.get(klass)).init(nc);
		}
		for(String beanName: ioc.getNames()) {
			ioc.get(null, beanName);
		}
	}

	public void destroy(NutConfig nc) {
		List<Class<?>> list = Scans.me().scanPackage(getClass(), "^.+Setup.class$");
		for (Class<?> klass : list) {
			if (klass == getClass())
				continue;
			((Setup) nc.getIoc().get(klass)).destroy(nc);
		}
	}

}
