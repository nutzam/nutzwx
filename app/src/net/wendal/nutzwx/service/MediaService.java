package net.wendal.nutzwx.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.resource.NutResource;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMaster;
import org.nutz.weixin.bean.WxMsgType;

@IocBean(create="init", depose="depose")
public class MediaService {
	
	private static final Log log = Logs.get();
	
	@Inject protected NutDaoWxContext wxctx;
	
	protected ExecutorService es;
	
	public void init() {
		es = Executors.newFixedThreadPool(8);
	}
	
	public void depose() {
		es.shutdown();
		try {
			es.awaitTermination(15, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.info("MediaService.es error?", e);
		}
	}

	public void loadFrom(final WxInMsg in) {
		if (in == null)
			return;
		final String mediaId = in.getMediaId();
		if (Strings.isBlank(mediaId))
			return;
		WxMsgType msgType = WxMsgType.valueOf(in.getMsgType());
		if (WxMsgType.image != msgType) {
			// 图片总是可以下载的, 其他就不是了
			WxMaster master = wxctx.get(in.getToUserName());
			if (master == null || Strings.isBlank(master.getAppid())) {
				return;
			}
		}
		es.submit(new Runnable() {
			public void run() {
				download(mediaId, in.getPicUrl());
			}
		});
	}
	
	protected void download(String media_id, String url) {
		
	}
	
	public NutResource get(String mediaId) {
		return null;
	}
}
