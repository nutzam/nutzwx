package org.nutz.weixin.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Xmls;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.mvc.WxEventType;
import org.nutz.weixin.mvc.WxMsgType;
import org.nutz.weixin.spi.WxHandler;

public class Wxs {

	public static WxInMsg convert(InputStream in) {
		Map<String, Object> map = Xmls.asMap(Xmls.xml(in).getDocumentElement());
		Map<String, Object> tmp = new HashMap<String, Object>();
		for (Entry<String, Object> en : map.entrySet()) {
			tmp.put(Strings.lowerFirst(en.getKey()), en.getValue());
		}
		return Lang.map2Object(tmp, WxInMsg.class);
	}
	
	public static boolean check(String token, String signature, String timestamp, String nonce) {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(token);
		tmp.add(timestamp);
		tmp.add(nonce);
		Collections.sort(tmp);
		return Lang.sha1(Lang.concat("", tmp)).equalsIgnoreCase(signature);
	}
	
	public static WxOutMsg handle(WxInMsg msg, WxHandler handler) {
		WxOutMsg out = null;
		switch (WxMsgType.valueOf(msg.getMsgType())) {
		case text:
			out = handler.text(msg);
			break;
		case image:
			out = handler.image(msg);
			break;
		case voice:
			out = handler.voice(msg);
			break;
		case video:
			out = handler.video(msg);
			break;
		case location:
			out = handler.location(msg);
			break;
		case link:
			out = handler.link(msg);
			break;
		case event:
			out = handleEvent(msg, handler);
			break;
		default:
			break;
		}
		return out;
	}
	
	public static WxOutMsg handleEvent(WxInMsg msg, WxHandler handler) {
		WxOutMsg out = null;
		switch (WxEventType.valueOf(msg.getEvent())) {
		case subscribe:
			out = handler.eventSubscribe(msg);
			break;
		case LOCATION:
			out = handler.eventLocation(msg);
			break;
		case SCAN:
			out = handler.eventScan(msg);
			break;
		case CLICK:
			out = handler.eventClick(msg);
			break;
		case VIEW:
			out = handler.eventView(msg);
			break;
		default:
			break;
		}
		return out;
	}
	
	public static void main(String[] args) {
		for (Object obj : WxMsgType.values()) {
			System.out.printf("WxOutMsg %s(WxInMsg msg);\n", obj);
		}
		for (Object obj : WxEventType.values()) {
			System.out.printf("WxOutMsg event%s(WxInMsg msg);\n", Strings.upperFirst(obj.toString().toLowerCase()));
		}
	}
}
