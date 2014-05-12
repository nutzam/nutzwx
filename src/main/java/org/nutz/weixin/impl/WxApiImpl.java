package org.nutz.weixin.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.json.Json;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxGroup;
import org.nutz.weixin.bean.WxMaster;
import org.nutz.weixin.bean.WxMenu;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.bean.WxUser;
import org.nutz.weixin.spi.WxAPI;
import org.nutz.weixin.util.Wxs;

@SuppressWarnings("unchecked")
public class WxApiImpl implements WxAPI {
	
	protected String base = "https://api.weixin.qq.com/cgi-bin";

	protected WxMaster master;

	public WxApiImpl(WxMaster master) {
		super();
		this.master = master;
	}

	public void send(WxOutMsg out) {
		if (out.getFromUserName() == null)
			out.setFromUserName(master.getOpenid());
		call("/message/custom/send", METHOD.POST, Wxs.asJson(out));
	}

	public WxGroup createGroup(WxGroup group) {
		Map<String, Object> map = call("/groups/create", METHOD.POST, Json.toJson(new NutMap().setv("group", group)));
		return Lang.map2Object((Map<String, Object>)map.get("group"), WxGroup.class);
	}

	public List<WxGroup> listGroup() {
		Map<String, Object> map = call("/groups/get", METHOD.GET, null);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("groups");
		List<WxGroup> groups = new ArrayList<WxGroup>();
		for (Map<String, Object> e : list) {
			groups.add(Lang.map2Object(e, WxGroup.class));
		}
		return groups;
	}

	public int userGroup(String openid) {
		Map<String, Object> map = call("/groups/getid", METHOD.POST, Json.toJson(new NutMap().setv("openid", openid)));
		return ((Number)map.get("groupid")).intValue();
	}

	public void renameGroup(WxGroup group) {
		call("/groups/update", METHOD.POST, Json.toJson(group));
	}

	public void moveUser2Group(String openid, String groupid) {
		call("/groups/members/update", METHOD.POST, Json.toJson(new NutMap().setv("openid", openid).setv("groupid", groupid)));
	}

	public WxUser fetchUser(String openid, String lang) {
		if (lang == null)
			lang = "zh_CN";
		Map<String, Object> map = call("/user/info?openid="+openid+"&lang="+openid, METHOD.GET, null);
		return Lang.map2Object(map, WxUser.class);
	}

	public void listWatcher(Each<String> each) {
		String next_openid = null;
		Map<String, Object> map = null;
		int count = 0;
		int total = 0;
		int index = 0;
		while (true) {
			if (next_openid == null)
				map = call("/user/get", METHOD.GET, null);
			else
				map = call("/user/get?next_openid="+next_openid, METHOD.GET, null);
			count = ((Number)map.get("count")).intValue();
			if (count < 1)
				return;
			total = ((Number)map.get("total")).intValue();
			next_openid = Strings.sNull(map.get("next_openid"));
			if (next_openid.length() == 0)
				next_openid = null;
			List<String> openids = (List<String>)((Map<String, Object>)map.get("data")).get("openid");
			for (String openid : openids) {
				try {
					each.invoke(index, openid, total);
				} catch (ExitLoop e) {
					return;
				} catch (ContinueLoop e) {
					continue;
				} catch (LoopException e) {
					throw e;
				}
				index++;
			}
		}
	}

	public void creatMenu(WxMenu menu) {
		call("/menu/create", METHOD.POST, Json.toJson(menu));
	}

	public WxMenu fetchMenu() {
		Map<String, Object> map = call("/menu/get", METHOD.GET, null);
		return Lang.map2Object((Map<String, Object>)map.get("menu"), WxMenu.class);
	}

	public void clearMenu() {
		call("/menu/clear", METHOD.GET, null);
	}

	public String tmpQr(int expire_seconds, String scene_id) {
		NutMap map = new NutMap().setv("expire_seconds", expire_seconds).setv("action_name", "QR_SCENE").setv("scene", new NutMap().setv("scene_id", scene_id));
		return call("", METHOD.POST, Json.toJson(map)).get("ticket").toString();
	}

	public String godQr(int scene_id) {
		NutMap map = new NutMap().setv("action_name", "QR_LIMIT_SCENE").setv("scene", new NutMap().setv("scene_id", scene_id));
		return call("", METHOD.POST, Json.toJson(map)).get("ticket").toString();
	}

	public String qrUrl(String ticket) {
		return base + "/showqrcode?ticket="+ticket;
	}

	public void reflushAccessToken() {
		String url = String.format("%s/token?grant_type=client_credential&appid=%s&secret=%s", base, master.getAppid(), master.getAppsecret());
		String str = Http.get(url).getContent();
		Map<String, Object> map = (Map<String, Object>) Json.fromJson(str);
		master.setAccess_token(map.get("access_token").toString());
		master.setAccess_token_expires(System.currentTimeMillis() + (((Number)map.get("expires_in")).intValue() - 60)  * 1000);
	}
	
	protected Map<String, Object> call(String URL, METHOD method, String body) {
		String token = master.getAccess_token();
		if (token == null || master.getAccess_token_expires() < System.currentTimeMillis()) {
			synchronized (master) {
				if (token == null || master.getAccess_token_expires() < System.currentTimeMillis()) {
					reflushAccessToken();
					token = master.getAccess_token();
				}
			}
		}
		if (URL.contains("?")) {
			URL = base + URL + "&access_token=" + token;
		} else {
			URL = base + URL + "?access_token=" + token;
		}
		Request req = Request.create(URL, method);
		if (body != null)
			req.setData(body);
		Response resp = Sender.create(req).send();
		if (!resp.isOK())
			throw new IllegalArgumentException("resp code=" + resp.getStatus());
		Map<String, Object> map = (Map<String, Object>) Json.fromJson(resp.getReader());
		if (map.containsKey("errcode") && ((Number)map.get("error")).intValue() != 0) {
			throw new IllegalArgumentException(map.toString());
		}
		return map;
	}
	
}
