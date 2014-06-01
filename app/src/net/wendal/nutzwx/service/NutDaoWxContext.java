package net.wendal.nutzwx.service;

import java.util.List;

import net.wendal.nutzwx.bean.WxMpInfo;
import net.wendal.nutzwx.bean.WxMsgHistory;
import net.wendal.nutzwx.bean.WxMsgStore;
import net.wendal.nutzwx.util.EnhandWxHandler;

import org.nutz.dao.Dao;
import org.nutz.dao.util.ExtDaos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
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
	
	@Inject protected MediaService mediaService;
	
	public void init() {
		List<WxMpInfo> list = dao.query(WxMpInfo.class, null);
		for (final WxMpInfo mp : list) {
			log.debug("mp info > " + mp.getOpenid());
			addMP(mp);
		}
	}
	
	protected WxOutMsg handle(WxHandler handler, WxInMsg in) {
		return Wxs.handle(in, handler);
	}
	
	public void addMP(final WxMpInfo mp) {
		if (mp == null || mp.getOpenid() == null)
			return;
		final Dao dao = ExtDaos.ext(this.dao, mp.getOpenid());
		dao.create(WxMsgHistory.class, false);
		dao.create(WxMsgStore.class, false);
		
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
		WxHandler handler = null;
		if (Strings.isBlank(mp.getHandlerClass())) {
			handler = new EnhandWxHandler();
		} else {
			try {
				handler = (WxHandler) Mvcs.ctx.getDefaultIoc().get(Class.forName(mp.getHandlerClass()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		if (handler instanceof EnhandWxHandler)
			((EnhandWxHandler)handler).setToken(mp.getToken());
		handlers.put(mp.getOpenid(), handler);
	}
}
