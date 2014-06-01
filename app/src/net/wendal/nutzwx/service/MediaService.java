package net.wendal.nutzwx.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMaster;
import org.nutz.weixin.bean.WxMedia;
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
		WxMaster master = wxctx.get(in.getToUserName());
		switch (msgType) {
		case video:
			es.submit(new Runnable() {
				public void run() {
					download(in.getToUserName(), in.getThumbMediaId(), null);
				}
			});
		default:
			if (Strings.isBlank(in.getMediaId()) || Strings.isBlank(master.getAppid()))
				return;
			es.submit(new Runnable() {
				public void run() {
					download(in.getToUserName(), mediaId, in.getPicUrl());
				}
			});
			break;
		}
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
					url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + wxctx.getAPI(openid).getAccessToken() + "&media_id=" + media_id;
				}
				Response resp = Http.get(url, 60*1000);
				if (resp.isOK()) {
					in = resp.getStream();
					tmp = File.createTempFile("nutzwx_media", ".media");
					out = new FileOutputStream(tmp);
					Streams.writeAndClose(out, in);
					// 检查一下是不是报错
					if (tmp.length() < 128) {
						byte[] data = Files.readBytes(f);
						if (data[0] == '{') { // 看上去是个json,悲催了...
							// 多媒体文件怎么可能是{开头,抛错吧
							throw new IllegalArgumentException("mediaId="+media_id+ ","+new String(data));
//							try {
//								NutMap map = Json.fromJson(NutMap.class, new String(data));
//								if (map.containsKey("errcode") && map.getInt("errcode") != 0)  {
//									log.warn("download media fail >> " + new String(data));
//								}
//							} catch (Throwable e) {
//								log.debug("not a json? ok", e);
//							}
						}
					}
					if (f.exists())
						f.delete();
					Files.makeDir(f.getParentFile());
					tmp.renameTo(f);
					WxMedia media = new WxMedia(media_id, resp.getHeader().getInt("Content-Length", 0), resp.getHeader().get("Content-Type"));
					Json.toJsonFile(new File(f.getAbsolutePath()+".info"), media);
					log.debug("media download success mediaId="+media_id);
					break;
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
	
	public WxMedia get(String openid, String mediaId){
		String path = mediaPath(openid, mediaId);
		File f = new File(path);
		if (!f.exists()) {
			log.info("Not exist " + f);
			return null;
		}
		WxMedia media = Json.fromJsonFile(WxMedia.class, new File(path + ".info"));
		try {
			media.setStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return media;
	}
	
	protected String mediaPath(String openid, String mediaId) {
		return String.format("%s/%s/%s/%s/%s", mediaRoot, openid,  mediaId.substring(0,2) ,mediaId.substring(2, 4) ,mediaId.substring(4));
	}
	
	public void save(String openid, WxMedia media, File f) {
		File target = new File(mediaPath(openid, media.getId()));
		Files.makeDir(target.getParentFile());
		if (f != null) {
			Files.copy(f, target);
			media.setSize(f.length());
		} else {
			Files.write(target, media.getStream());
		}
		Json.toJsonFile(new File(target.getAbsolutePath() + ".info"), media);
		log.info("save to " + target);
	}
}
