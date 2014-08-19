package net.wendal.iot.mvc;

import java.util.List;

import net.wendal.iot.Iots;
import net.wendal.iot.bean.IotUser;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

@IocBean
public class IotSetup implements Setup {

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
	}
	
	@Override
	public void destroy(NutConfig nc) {
	}
}
