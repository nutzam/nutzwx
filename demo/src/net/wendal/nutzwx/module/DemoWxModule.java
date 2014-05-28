package net.wendal.nutzwx.module;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.mvc.WxAbstractModule;
import org.nutz.weixin.util.Wxs;

/**
 * 只需要继承WxAbstractModule,就能获取对应的入口方法
 */
@IocBean(fields="wxHandler") // 需要注入处理类.真正的入口方法在WxAbstractModule里面已经定义好了
public class DemoWxModule extends WxAbstractModule {

	public DemoWxModule() {
		Wxs.enableDevMode(); // 开启debug模式,这样就会把接收和发送的内容统统打印,方便查看
	}

	// 在WxAbstractModule里面入口方法就是msgIn, 你可以覆盖,然后定义新的路径
//	@At({"/wwwww", "/wwwwwwwwwww/?"}) // key是为了支持多公众帐号,不填也行啦
//	@Fail("http:200") // 建议出错时直接返回200,这样微信服务器就不会重试
//	public View msgIn(String key, HttpServletRequest req) throws IOException {
//		return super.msgIn(key, req);
//	}
	
	// 获取WxHandler的方法,对应msgIn的key参数和注入的WxHandler对象.
//	public WxHandler getWxHandler(String key) {
//		return super.getWxHandler(key);
//	}
}
