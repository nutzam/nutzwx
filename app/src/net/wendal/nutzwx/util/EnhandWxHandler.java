package net.wendal.nutzwx.util;

import net.wendal.nutzwx.service.MediaService;
import net.wendal.nutzwx.service.WxHistoryService;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.util.Wxs;

public class EnhandWxHandler extends BasicWxHandler {
	
	protected WxHistoryService wxHistory;
	protected MediaService mediaService;
	
	public EnhandWxHandler() {
		super(null);
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
	
	public void setWxHistory(WxHistoryService wxHistory) {
		this.wxHistory = wxHistory;
	}
	
	public void init() {}
	
	public WxOutMsg handle(WxInMsg in) {
		wxHistory.push(in); // 插入到历史记录
		mediaService.loadFrom(in); // 查找media,如果存在就下载之
		WxOutMsg out = super.handle(in);
		if (out != null)
			wxHistory.push(out);
		return out;
	}
	
	public WxOutMsg defaultMsg(WxInMsg msg) {
		return Wxs.respText(null, Json.toJson(msg, JsonFormat.compact()));
	}
}
