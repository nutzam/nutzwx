package net.wendal.nutzwx.module;

import net.wendal.nutzwx.service.NutDaoWxContext;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.mvc.WxAbstractModule;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

@IocBean(fields="wxHandler")
public class BasicWxModule extends WxAbstractModule {

	public BasicWxModule() {
	}

	public BasicWxModule(boolean devMode) {
		if (devMode)
			Wxs.enableDevMode();
	}
	
	@Inject protected NutDaoWxContext ctx;
	
	@Override
	public WxHandler getWxHandler(String key) {
		return ctx.getHandler(key);
	}
}
