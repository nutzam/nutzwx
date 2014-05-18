package net.wendal.nutzwx.service;

import java.util.List;

import net.wendal.nutzwx.bean.WxMpInfo;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.impl.WxApiImpl;
import org.nutz.weixin.util.WxContext;

@IocBean(name="wxctx", create="init")
public class NutDaoWxContext extends WxContext {

	@Inject protected Dao dao;
	
	public void init() {
		List<WxMpInfo> list = dao.query(WxMpInfo.class, null);
		for (WxMpInfo mp : list) {
			masters.put(mp.getOpenid(), mp);
			apis.put(mp.getOpenid(), new WxApiImpl(mp));
		}
	}
}
