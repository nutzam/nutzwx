package net.wendal.nutzwx;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.weixin.mvc.WxViewMaker;

@Modules(scanPackage=true)
@IocBy(type=ComboIocProvider.class, args={"*js", "ioc/", "*annotation", "net.wendal.nutzwx"})
@Views(WxViewMaker.class)
public class MainModule {
}
