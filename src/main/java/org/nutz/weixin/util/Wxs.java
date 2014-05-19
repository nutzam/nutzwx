package org.nutz.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Xmls;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.HttpStatusView;
import org.nutz.mvc.view.RawView;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.weixin.bean.WxArticle;
import org.nutz.weixin.bean.WxEventType;
import org.nutz.weixin.bean.WxImage;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMsgType;
import org.nutz.weixin.bean.WxMusic;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.bean.WxVideo;
import org.nutz.weixin.bean.WxVoice;
import org.nutz.weixin.mvc.WxView;
import org.nutz.weixin.spi.WxHandler;

public class Wxs {
	
	private static final Log log = Logs.get();
	
	private static boolean DEV_MODE = false;
	
	public static void enableDevMode() {
		DEV_MODE = true;
		log.warn("nutzwx DevMode=true now");
	}

	public static WxInMsg convert(InputStream in) {
		Map<String, Object> map = Xmls.asMap(Xmls.xml(in).getDocumentElement());
		Map<String, Object> tmp = new HashMap<String, Object>();
		for (Entry<String, Object> en : map.entrySet()) {
			tmp.put(Strings.lowerFirst(en.getKey()), en.getValue());
		}
		if (DEV_MODE) {
			log.debug("Income >> \n" + Json.toJson(map));
		}
		return Lang.map2Object(tmp, WxInMsg.class);
	}
	
	public static boolean check(String token, String signature, String timestamp, String nonce) {
		// 防范长密文攻击
		if (signature == null || signature.length() > 128 
				|| timestamp == null || timestamp.length() > 128
				|| nonce == null || nonce.length() > 128) {
			log.warnf("bad check : signature=%s,timestamp=%s,nonce=%s", signature, timestamp, nonce);
			return false;
		}
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(token);
		tmp.add(timestamp);
		tmp.add(nonce);
		Collections.sort(tmp);
		String key = Lang.concat("", tmp).toString();
		return Lang.sha1(key).equalsIgnoreCase(signature);
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
			log.infof("New MsyType=%s ? fallback to defaultMsg", msg.getMsgType());
			out = handler.defaultMsg(msg);
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
			log.infof("New EventType=%s ? fallback to defaultMsg", msg.getMsgType());
			out = handler.defaultMsg(msg);
			break;
		}
		return out;
	}
	
	public static WxOutMsg fix(WxInMsg in, WxOutMsg out) {
		out.setFromUserName(in.getToUserName());
		out.setToUserName(in.getFromUserName());
		out.setCreateTime(System.currentTimeMillis()/1000);
		return out;
	}
	
	public static WxOutMsg respText(String content) {
		WxOutMsg out = new WxOutMsg("text");
		out.setContent(content);
		return out;
	}
	
	public static WxOutMsg respImage(String mediaId) {
		WxOutMsg out = new WxOutMsg("image");
		out.setImage(new WxImage(mediaId));
		return out;
	}
	
	public static WxOutMsg respVoice(String mediaId) {
		WxOutMsg out = new WxOutMsg("voice");
		out.setVoice(new WxVoice(mediaId));
		return out;
	}
	
	public static WxOutMsg respVideo(String mediaId, String title, String description) {
		WxOutMsg out = new WxOutMsg("video");
		out.setVideo(new WxVideo(mediaId, title, description));
		return out;
	}
	
	public static WxOutMsg respMusic(String title, String description, String musicURL, String hQMusicUrl, String thumbMediaId) {
		WxOutMsg out = new WxOutMsg("music");
		out.setMusic(new WxMusic(title, description, musicURL, hQMusicUrl, thumbMediaId));
		return out;
	}
	
	public static WxOutMsg respNews(WxArticle...articles) {
		return respNews(Arrays.asList(articles));
	}
	
	public static WxOutMsg respNews(List<WxArticle> articles) {
		WxOutMsg out = new WxOutMsg("news");
		out.setArticles(articles);
		return out;
	}
	
//	public static StringBuilder toWxXml(WxOutMsg out) {
//		Map<String, Object> map = Lang.obj2map(out);
//		StringBuilder sb = new StringBuilder();
//		sb.append("<xml>\n");
//		toWxXml(sb, map);
//		sb.append("</xml>");
//		return sb;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static void toWxXml(StringBuilder sb, Map<String, Object> map) {
//		for (Entry<String, Object> en : map.entrySet()) {
//			Object obj = en.getValue();
//			if (obj == null)
//				continue;
//			if (obj instanceof Number && ((Number)obj).intValue() == 0) {
//				continue;
//			}
//			sb.append("<" + Strings.upperFirst(en.getKey()) + ">");
//			if (obj instanceof String) {
//				sb.append("<![CDATA[").append(obj).append("]]>");
//			} else if (obj instanceof Number) {
//				sb.append(obj);
//			} else if (obj instanceof Map) {
//				sb.append("\n");
//				toWxXml(sb, ((Map<String, Object>)obj));
//			} else if (obj instanceof List) {
//				sb.append("\n");
//				for (Object _obj : ((Collection<Object>)obj)) {
//					toWxXml(sb, ((Map<String, Object>)_obj));
//				}
//			} else {
//				throw Lang.noImplement();
//			}
//			sb.append("</" + Strings.upperFirst(en.getKey()) + ">\n");
//		}
//	}
	
	public static String cdata(String str) {
		return "<![CDATA[" + str + "]]>";
	}
	
	public static String tag(String key, String val) {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(key).append(">");
		sb.append(val).append("");
		sb.append("</").append(key).append(">\n");
		return sb.toString();
	}
	
	public static void asXml(Writer writer, WxOutMsg msg) throws IOException {
		Writer _out = writer;
		if (DEV_MODE) {
			writer = new StringWriter();
		}
		writer.write("<xml>\n");
		writer.write(tag("ToUserName", cdata(msg.getToUserName())));
		writer.write(tag("FromUserName", cdata(msg.getFromUserName())));
		writer.write(tag("CreateTime", ""+msg.getCreateTime()));
		writer.write(tag("MsgType", cdata(msg.getMsgType())));
		switch (WxMsgType.valueOf(msg.getMsgType())) {
		case text:
			writer.write(tag("Content", cdata(msg.getContent())));
			break;
		case image:
			writer.write(tag("Image", tag("MediaId", msg.getImage().getMediaId())));
			break;
		case voice:
			writer.write(tag("Voice", tag("MediaId", msg.getVoice().getMediaId())));
			break;
		case video:
			writer.write("<Video>\n");
			writer.write(tag("MediaId", cdata(msg.getVideo().getMediaId())));
			if (msg.getVideo().getTitle() != null)
				writer.write(tag("Title", cdata(msg.getVideo().getTitle())));
			if (msg.getVideo().getDescription() != null)
				writer.write(tag("Description", cdata(msg.getVideo().getDescription())));
			writer.write("</Video>\n");
			break;
		case music :
			writer.write("<Music>\n");
			WxMusic music = msg.getMusic();
			if (music.getTitle() != null)
				writer.write(tag("Title", cdata(music.getTitle())));
			if (music.getDescription() != null)
				writer.write(tag("Description", cdata(music.getDescription())));
			if (music.getMusicUrl() != null)
				writer.write(tag("MusicUrl", cdata(music.getMusicUrl())));
			if (music.getHQMusicUrl() != null)
				writer.write(tag("HQMusicUrl", cdata(music.getHQMusicUrl())));
			writer.write(tag("ThumbMediaId", cdata(music.getThumbMediaId())));
			writer.write("</Music>\n");
			break;
		case news:
			writer.write(tag("ArticleCount", ""+msg.getArticles().size()));
			writer.write("<Articles>\n");
			for (WxArticle article : msg.getArticles()) {
				writer.write("<item>\n");
				if (article.getTitle() != null)
					writer.write(tag("Title", cdata(article.getTitle())));
				if (article.getDescription() != null)
					writer.write(tag("Description", cdata(article.getDescription())));
				if (article.getPicUrl() != null)
					writer.write(tag("PicUrl", cdata(article.getPicUrl())));
				if (article.getUrl() != null)
					writer.write(tag("Url", cdata(article.getUrl())));
				writer.write("</item>\n");
			}
			writer.write("</Articles>\n");
			break;
		default:
			break;
		}
		writer.write("</xml>");
		if (DEV_MODE) {
			String str = writer.toString();
			log.debug("Outcome >>\n" + str);
			_out.write(str);
		}
	}
	
	public static String asJson(WxOutMsg msg) {
		StringWriter sw = new StringWriter();
		asJson(sw, msg);
		return sw.toString();
	}
	
	public static void asJson(Writer writer, WxOutMsg msg) {
		NutMap map = new NutMap();
		map.put("touser", msg.getToUserName());
		map.put("msgtype", msg.getMsgType());
		switch (WxMsgType.valueOf(msg.getMsgType())) {
		case text:
			map.put("text", new NutMap().setv("content", msg.getContent()));
			break;
		case image :
			map.put("image", new NutMap().setv("media_id", msg.getImage().getMediaId()));
			break;
		case voice:
			map.put("voice", new NutMap().setv("media_id", msg.getVoice().getMediaId()));
			break;
		case video:
			NutMap _video = new NutMap();
			_video.setv("media_id", msg.getVideo().getMediaId());
			if (msg.getVideo().getTitle() != null)
				_video.put("title", cdata(msg.getVideo().getTitle()));
			if (msg.getVideo().getDescription() != null)
				_video.put("description", cdata(msg.getVideo().getDescription()));
			map.put("video", _video);
			break;
		case music :
			NutMap _music = new NutMap();
			WxMusic music = msg.getMusic();
			if (music.getTitle() != null)
				_music.put("title", cdata(music.getTitle()));
			if (music.getDescription() != null)
				_music.put("description", cdata(music.getDescription()));
			if (music.getMusicUrl() != null)
				_music.put("musicurl", cdata(music.getMusicUrl()));
			if (music.getHQMusicUrl() != null)
				_music.put("hqmusicurl", cdata(music.getHQMusicUrl()));
			_music.put("thumb_media_id", cdata(music.getThumbMediaId()));
			break;
		case news:
			NutMap _news = new NutMap();
			List<NutMap> list = new ArrayList<NutMap>();
			for (WxArticle article : msg.getArticles()) {
				NutMap item = new NutMap();
				if (article.getTitle() != null)
					item.put("title", cdata(article.getTitle()));
				if (article.getDescription() != null)
					item.put("description", cdata(article.getDescription()));
				if (article.getPicUrl() != null)
					item.put("picUrl", cdata(article.getPicUrl()));
				if (article.getUrl() != null)
					item.put("url", cdata(article.getUrl()));
				list.add(item);
			}
			_news.put("articles", list);
			map.put("news", _news);
			break;
		default:
			break;
		}
		Json.toJson(writer, map);
	}
	
	public static View handle(WxHandler wxHandler, HttpServletRequest req) throws IOException {
		if (wxHandler == null) {
			return HttpStatusView.HTTP_502;
		}
		if (!wxHandler.check(req.getParameter("signature"), req.getParameter("timestamp"), req.getParameter("nonce"))) {
			return HttpStatusView.HTTP_502;
		}
		if ("GET".equalsIgnoreCase(req.getMethod())) {
			return new ViewWrapper(new RawView(null), req.getParameter("echostr"));
		}
		WxInMsg in = Wxs.convert(req.getInputStream());
		WxOutMsg out = wxHandler.handle(in);
		if (out != null)
			Wxs.fix(in, out);
		return new ViewWrapper(WxView.me, out);
	}
	
	public static void main(String[] args) throws IOException {
		for (Object obj : WxMsgType.values()) {
			System.out.printf("WxOutMsg %s(WxInMsg msg);\n", obj);
		}
		for (Object obj : WxEventType.values()) {
			System.out.printf("WxOutMsg event%s(WxInMsg msg);\n", Strings.upperFirst(obj.toString().toLowerCase()));
		}
		StringWriter sw = new StringWriter();
		asXml(sw, respText("Hi"));
		System.out.println(sw.toString());
	}
}
