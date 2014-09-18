package net.wendal;

import java.util.Date;
import java.util.List;

import net.wendal.base.bean.User;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

@IocBean
public class MainSetup implements Setup {
	
	private static final Log log = Logs.get();

	@Inject Dao dao;
	@Inject("refer:$ioc") Ioc ioc;
	
	public void init(NutConfig nc) {
		Daos.createTablesInPackage(dao, User.class.getPackage().getName(), false);
		if (dao.count(User.class) == 0) {
			log.info("create root user");
			User root = new User();
			root.setName("root");
			root.setAlias("root");
			root.setCreateTime(new Date());
			root.setEmail("wendal1985@gmail.com");
			root.setSlat(R.UU64());
			root.setPasswd(Lang.sha1(root.getSlat() + "123456" + root.getSlat()));
			dao.insert(root);
		}
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
