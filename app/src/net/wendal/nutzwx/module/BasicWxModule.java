package net.wendal.nutzwx.module;

import net.wendal.nutzwx.service.NutDaoWxContext;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.mvc.WxAbstractModule;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;

@IocBean()
public class BasicWxModule extends WxAbstractModule {

	public BasicWxModule() {
		Wxs.enableDevMode();
	}
	
	@Inject protected NutDaoWxContext wxctx;
	
	@Override
	public WxHandler getWxHandler(String key) {
		return wxctx.getHandler(key);
	}
	
	
}
