package net.wendal.nutzwx.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMaster;
import org.nutz.weixin.bean.WxMsgType;

@IocBean(create="init", depose="depose")
public class MediaService {
	
	private static final Log log = Logs.get();
	
	@Inject protected NutDaoWxContext wxctx;
	
	@Inject("java:$config.get('media.root')") protected String mediaRoot;
	
	protected ExecutorService es;
	
	protected Map<String, Object> lock = new HashMap<>();
	
	public void init() {
		es = Executors.newFixedThreadPool(8);
		log.info("media root=" + new File(mediaRoot).getAbsolutePath());
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
				download(in.getToUserName(), mediaId, in.getPicUrl());
			}
		});
	}
	
	protected void download(String openid, String media_id, String url) {
		File f = new File(mediaPath(openid, media_id));
		if (f.exists() && f.length() > 1) {
			log.info("Media aready exit > " + media_id);
			return;
		}
		for (int i = 0; i < 3; i++) {
			InputStream in = null;
			OutputStream out = null;
			File tmp = null;
			try {
				if (Strings.isBlank(url)) {
					WxMaster master = wxctx.get(openid);
					url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + master.getAccess_token() + "&media_id" + media_id;
				}
				Response resp = Http.get(url, 60*1000);
				if (resp.isOK()) {
					in = resp.getStream();
					tmp = File.createTempFile("nutzwx_media", ".media");
					out = new FileOutputStream(tmp);
					Streams.writeAndClose(out, in);
					if (f.exists())
						f.delete();
					Files.makeDir(f.getParentFile());
					tmp.renameTo(f);
				} else {
					log.debugf("download %s fail, code=%s, content=%s", media_id, resp.getStatus(), resp.getContent());
				}
			} catch (Throwable e) {
				log.infof("download %s fail", media_id, e);
			} finally {
				Streams.safeClose(in);
				Streams.safeClose(out);
				if (tmp != null)
					tmp.delete();
			}
		}
	}
	
	public InputStream get(String openid, String mediaId) throws IOException {
		File f = new File(mediaPath(openid, mediaId));
		if (!f.exists())
			return null;
		return new FileInputStream(f);
	}
	
	protected String mediaPath(String openid, String mediaId) {
		return String.format("%s/%s/%s/%s/%s", mediaRoot, openid,  mediaId.substring(0,2) ,mediaId.substring(2, 4) ,mediaId.substring(4));
	}
}
