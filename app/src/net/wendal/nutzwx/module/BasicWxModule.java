package net.wendal.nutzwx.module;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.mvc.WxAbstractModule;
import org.nutz.weixin.util.Wxs;

@IocBean(fields="wxHandler")
public class BasicWxModule extends WxAbstractModule {

	public BasicWxModule() {
	}

	public BasicWxModule(boolean devMode) {
		if (devMode)
			Wxs.enableDevMode();
	}
}
