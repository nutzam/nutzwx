package net.wendal.nutzwx;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.util.Wxs;

/**
 * 这个类是注入到DemoWxModule的,非常非常重要
 *
 */
@IocBean(name="wxHandler", args={"9b6b3a8ae"}) // 这里的9b6b3a8ae就是页面上填的token,这样写,是因为了本demo无需配置文件
public class DemoWxHandler extends BasicWxHandler {

	public DemoWxHandler(String token) {
		super(token);
	}

	// 用户发送的是文本的时候调用这个方法
	public WxOutMsg text(WxInMsg msg) {
		if ("god".equals(msg.getContent())) // 用户输入god,我就惊叹一下嘛!
			return Wxs.respText(null, "Oh my God!");
		else 
			return Wxs.respText(null, "Out of my way!"); // 否则,滚开!
	}
	
	// 微信有很多种信息,你可以覆盖对应的方法进行处理
//	public WxOutMsg location(WxInMsg msg) {
//		return super.location(msg);
//	}
	
	// 在BasicWxHandler中,所有类型的信息,最终都是调用这个默认信息,so,你懂啦
//	public WxOutMsg defaultMsg(WxInMsg msg) {
//		return Wxs.respText(null, "Hi, wendal");
//	}
	
	// 不打算校验signature? 把下面的代码启用,使check永真. 生产环境绝对禁止使用啦!
//	public boolean check(String signature, String timestamp, String nonce) {
//		return true;
//	}
}
