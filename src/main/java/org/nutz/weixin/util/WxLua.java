package org.nutz.weixin.util;

import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxAPI;

/**
 * 辅助Lua调用的类,仅供lua代码调用!
 * @author Administrator
 *
 */
public class WxLua {

	public static ThreadLocal<WxAPI> wxapi = new ThreadLocal<>();
	
	public static void sendText(String uid, String text) {
		WxOutMsg out = Wxs.respText(text);
		out.setToUserName(uid);
		wxapi.get().send(out);
	}
	
	public static void test() {
		System.out.println("Hi,lua!");
	}
}
