package net.wendal.nutzwx;

import net.wendal.nutzwx.bean.User;
import net.wendal.nutzwx.util.Toolkit;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

public class WxSetup implements Setup {
	
	private static final Log log = Logs.get();

	@Override
	public void init(NutConfig nc) {
		Dao dao = nc.getIoc().get(Dao.class);
		Daos.createTablesInPackage(dao, User.class.getPackage().getName(), false);
		if (dao.count(User.class) == 0) {
			User user = new User();
			String passwd = R.UU16();
			String slat = R.sg(48).next();
			user.setName("admin");
			user.setPasswd(Toolkit.passwordEncode(passwd, slat));
			user.setSlat(slat);
			dao.insert(user);
			log.warn("init admin usr as passwd " + passwd);
		}
	}

	@Override
	public void destroy(NutConfig nc) {
		
	}

}
