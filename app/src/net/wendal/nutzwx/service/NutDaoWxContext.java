package net.wendal.nutzwx.service;

import java.util.List;

import net.wendal.nutzwx.bean.WxMpInfo;
import net.wendal.nutzwx.bean.WxMsgHistory;

import org.nutz.dao.Dao;
import org.nutz.dao.TableName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.impl.WxApiImpl;
import org.nutz.weixin.spi.WxAPI;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.WxContext;
import org.nutz.weixin.util.Wxs;

@IocBean(name="wxctx", create="init")
public class NutDaoWxContext extends WxContext {
	
	private static final Log log = Logs.get();

	@Inject protected Dao dao;
	
	@Inject protected WxHistoryService wxHistory;
	
	public void init() {
		List<WxMpInfo> list = dao.query(WxMpInfo.class, null);
		for (final WxMpInfo mp : list) {
			log.debug("mp info > " + mp.getOpenid());
			masters.put(mp.getOpenid(), mp);
			WxAPI api = new WxApiImpl(mp) {
				public void reflushAccessToken() {
					super.reflushAccessToken();
					dao.update(mp, "^(access_token)");
				}
				@Override
				public void send(WxOutMsg out) {
					super.send(out);
					wxHistory.push(out);
				}
			};
			apis.put(mp.getOpenid(), api);
			handlers.put(mp.getOpenid(), new BasicWxHandler(mp.getToken()) {
				public WxOutMsg handle(WxInMsg in) {
					wxHistory.push(in);
					WxOutMsg out = NutDaoWxContext.this.handle(this, in);
					if (out != null)
						wxHistory.push(out);
					return out;
				}
				public WxOutMsg defaultMsg(WxInMsg msg) {
					return Wxs.respText(null, Json.toJson(msg, JsonFormat.compact()));
				}
			});
			TableName.run(mp.getOpenid(), new Runnable() {
				public void run() {
					dao.create(WxMsgHistory.class, false);
				}
			});
		}
	}
	
	protected WxOutMsg handle(WxHandler handler, WxInMsg in) {
		return Wxs.handle(in, handler);
	}
}
