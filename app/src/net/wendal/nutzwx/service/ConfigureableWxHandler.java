package net.wendal.nutzwx.service;

import net.wendal.nutzwx.bean.WxMpInfo;

import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.spi.WxAPI;

public class ConfigureableWxHandler extends BasicWxHandler {
	
	protected NutDaoWxContext ctx;
	
	protected WxMpInfo mp;
	
	protected WxAPI api;

	public ConfigureableWxHandler(NutDaoWxContext ctx, WxMpInfo mp, WxAPI api) {
		super(mp.getToken());
		this.ctx = ctx;
		this.mp = mp;
		this.api = api;
	}

	@Override
	public WxOutMsg handle(WxInMsg in) {
		
		return super.handle(in);
	}

	
}
