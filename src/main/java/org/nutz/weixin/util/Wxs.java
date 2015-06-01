package org.nutz.weixin.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
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

    public static boolean DEV_MODE = false;

    public static void enableDevMode() {
        DEV_MODE = true;
        log.warn("nutzwx DevMode=true now");
    }

    /**
     * 将一个输入流转为WxInMsg
     */
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

    public static WxInMsg convert(String data) {
        return convert(new ByteArrayInputStream(data.getBytes()));
    }

    /**
     * 检查signature是否合法
     */
    public static boolean check(String token, String signature, String timestamp, String nonce) {
        // 防范长密文攻击
        if (signature == null
            || signature.length() > 128
            || timestamp == null
            || timestamp.length() > 128
            || nonce == null
            || nonce.length() > 128) {
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

    /**
     * 根据不同的消息类型,调用WxHandler不同的方法
     */
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

    /**
     * 根据msg中Event的类型,调用不同的WxHandler方法
     */
    public static WxOutMsg handleEvent(WxInMsg msg, WxHandler handler) {
        WxOutMsg out = null;
        switch (WxEventType.valueOf(msg.getEvent())) {
        case subscribe:
            out = handler.eventSubscribe(msg);
            break;
        case unsubscribe:
            out = handler.eventUnsubscribe(msg);
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
        case TEMPLATESENDJOBFINISH:
            out = handler.eventTemplateJobFinish(msg);
            break;
        default:
            log.infof("New EventType=%s ? fallback to defaultMsg", msg.getMsgType());
            out = handler.defaultMsg(msg);
            break;
        }
        return out;
    }

    /**
     * 根据输入信息,修正发送信息的发送者和接受者
     */
    public static WxOutMsg fix(WxInMsg in, WxOutMsg out) {
        out.setFromUserName(in.getToUserName());
        out.setToUserName(in.getFromUserName());
        out.setCreateTime(System.currentTimeMillis() / 1000);
        return out;
    }

    /**
     * 创建一条文本响应
     */
    public static WxOutMsg respText(String to, String content) {
        WxOutMsg out = new WxOutMsg("text");
        out.setContent(content);
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    /**
     * 创建一条图片响应
     */
    public static WxOutMsg respImage(String to, String mediaId) {
        WxOutMsg out = new WxOutMsg("image");
        out.setImage(new WxImage(mediaId));
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    /**
     * 创建一个语音响应
     */
    public static WxOutMsg respVoice(String to, String mediaId) {
        WxOutMsg out = new WxOutMsg("voice");
        out.setVoice(new WxVoice(mediaId));
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    /**
     * 创建一个视频响应
     */
    public static WxOutMsg respVideo(String to, String mediaId, String title, String description) {
        WxOutMsg out = new WxOutMsg("video");
        out.setVideo(new WxVideo(mediaId, title, description));
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    /**
     * 创建一个音乐响应
     */
    public static WxOutMsg respMusic(String to,
                                     String title,
                                     String description,
                                     String musicURL,
                                     String hQMusicUrl,
                                     String thumbMediaId) {
        WxOutMsg out = new WxOutMsg("music");
        out.setMusic(new WxMusic(title, description, musicURL, hQMusicUrl, thumbMediaId));
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    /**
     * 创建一个图文响应
     */
    public static WxOutMsg respNews(String to, WxArticle... articles) {
        return respNews(to, Arrays.asList(articles));
    }

    /**
     * 创建一个图文响应
     */
    public static WxOutMsg respNews(String to, List<WxArticle> articles) {
        WxOutMsg out = new WxOutMsg("news");
        out.setArticles(articles);
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    // public static StringBuilder toWxXml(WxOutMsg out) {
    // Map<String, Object> map = Lang.obj2map(out);
    // StringBuilder sb = new StringBuilder();
    // sb.append("<xml>\n");
    // toWxXml(sb, map);
    // sb.append("</xml>");
    // return sb;
    // }
    //
    // @SuppressWarnings("unchecked")
    // public static void toWxXml(StringBuilder sb, Map<String, Object> map) {
    // for (Entry<String, Object> en : map.entrySet()) {
    // Object obj = en.getValue();
    // if (obj == null)
    // continue;
    // if (obj instanceof Number && ((Number)obj).intValue() == 0) {
    // continue;
    // }
    // sb.append("<" + Strings.upperFirst(en.getKey()) + ">");
    // if (obj instanceof String) {
    // sb.append("<![CDATA[").append(obj).append("]]>");
    // } else if (obj instanceof Number) {
    // sb.append(obj);
    // } else if (obj instanceof Map) {
    // sb.append("\n");
    // toWxXml(sb, ((Map<String, Object>)obj));
    // } else if (obj instanceof List) {
    // sb.append("\n");
    // for (Object _obj : ((Collection<Object>)obj)) {
    // toWxXml(sb, ((Map<String, Object>)_obj));
    // }
    // } else {
    // throw Lang.noImplement();
    // }
    // sb.append("</" + Strings.upperFirst(en.getKey()) + ">\n");
    // }
    // }

    public static String cdata(String str) {
        if (Strings.isBlank(str))
            return "";
        return "<![CDATA[" + str.replaceAll("]]", "__") + "]]>";
    }

    public static String tag(String key, String val) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(key).append(">");
        sb.append(val).append("");
        sb.append("</").append(key).append(">\n");
        return sb.toString();
    }

    /**
     * @see #asXml(Writer, WxOutMsg)
     */
    public static String asXml(WxOutMsg msg) {
        StringWriter sw = new StringWriter();
        asXml(sw, msg);
        return sw.toString();
    }

    /**
     * 将一个WxOutMsg转为被动响应所需要的XML文本
     * 
     * @param msg
     *            微信消息输出对象
     * 
     * @return 输出的 XML 文本
     */
    public static void asXml(Writer writer, WxOutMsg msg) {
        try {
            Writer _out = writer;
            if (DEV_MODE) {
                writer = new StringWriter();
            }
            writer.write("<xml>\n");
            writer.write(tag("ToUserName", cdata(msg.getToUserName())));
            writer.write(tag("FromUserName", cdata(msg.getFromUserName())));
            writer.write(tag("CreateTime", "" + msg.getCreateTime()));
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
            case music:
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
                writer.write(tag("ArticleCount", "" + msg.getArticles().size()));
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
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * @see #asJson(Writer, WxOutMsg)
     */
    public static String asJson(WxOutMsg msg) {
        StringWriter sw = new StringWriter();
        asJson(sw, msg);
        return sw.toString();
    }

    /**
     * 将一个WxOutMsg转为主动信息所需要的Json文本
     * 
     * @param msg
     *            微信消息输出对象
     * 
     * @return 输出的 JSON 文本
     */
    public static void asJson(Writer writer, WxOutMsg msg) {
        NutMap map = new NutMap();
        map.put("touser", msg.getToUserName());
        map.put("msgtype", msg.getMsgType());
        switch (WxMsgType.valueOf(msg.getMsgType())) {
        case text:
            map.put("text", new NutMap().setv("content", msg.getContent()));
            break;
        case image:
            map.put("image", new NutMap().setv("media_id", msg.getImage().getMediaId()));
            break;
        case voice:
            map.put("voice", new NutMap().setv("media_id", msg.getVoice().getMediaId()));
            break;
        case video:
            NutMap _video = new NutMap();
            _video.setv("media_id", msg.getVideo().getMediaId());
            if (msg.getVideo().getTitle() != null)
                _video.put("title", (msg.getVideo().getTitle()));
            if (msg.getVideo().getDescription() != null)
                _video.put("description", (msg.getVideo().getDescription()));
            map.put("video", _video);
            break;
        case music:
            NutMap _music = new NutMap();
            WxMusic music = msg.getMusic();
            if (music.getTitle() != null)
                _music.put("title", (music.getTitle()));
            if (music.getDescription() != null)
                _music.put("description", (music.getDescription()));
            if (music.getMusicUrl() != null)
                _music.put("musicurl", (music.getMusicUrl()));
            if (music.getHQMusicUrl() != null)
                _music.put("hqmusicurl", (music.getHQMusicUrl()));
            _music.put("thumb_media_id", (music.getThumbMediaId()));
            break;
        case news:
            NutMap _news = new NutMap();
            List<NutMap> list = new ArrayList<NutMap>();
            for (WxArticle article : msg.getArticles()) {
                NutMap item = new NutMap();
                if (article.getTitle() != null)
                    item.put("title", (article.getTitle()));
                if (article.getDescription() != null)
                    item.put("description", (article.getDescription()));
                if (article.getPicUrl() != null)
                    item.put("picurl", (article.getPicUrl()));
                if (article.getUrl() != null)
                    item.put("url", (article.getUrl()));
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

    /**
     * 用一个wxHandler处理对应的用户请求
     */
    public static View handle(WxHandler wxHandler, HttpServletRequest req, String key)
            throws IOException {
        if (wxHandler == null) {
            log.info("WxHandler is NULL");
            return HttpStatusView.HTTP_502;
        }
        if (!wxHandler.check(req.getParameter("signature"),
                             req.getParameter("timestamp"),
                             req.getParameter("nonce"),
                             key)) {
            log.info("token is invalid");
            return HttpStatusView.HTTP_502;
        }
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            log.info("GET? return echostr=" + req.getParameter("echostr"));
            return new ViewWrapper(new RawView(null), req.getParameter("echostr"));
        }
        WxInMsg in = Wxs.convert(req.getInputStream());
        in.setExtkey(key);
        WxOutMsg out = wxHandler.handle(in);
        if (out != null) {
            Wxs.fix(in, out);
        }
        return new ViewWrapper(WxView.me, out);
    }

    /**
     * 下载媒体文件(放到临时目录中), 返回对应文件
     * 
     * @param accessToken
     * @param mediaId
     */
    public static File downloadMedia(String accessToken, String mediaId) {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
                     + accessToken
                     + "&media_id="
                     + mediaId;
        File mf = null;
        for (int i = 0; i < 3; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                Response resp = Http.get(url, 60 * 1000);
                if (resp.isOK()) {
                    in = resp.getStream();
                    mf = File.createTempFile(mediaId, ".wxmedia");
                    out = new FileOutputStream(mf);
                    Streams.writeAndClose(out, in);
                    // 检查一下是不是报错
                    if (mf.length() < 128) {
                        byte[] data = Files.readBytes(mf);
                        if (data[0] == '{') { // 看上去是个json,悲催了...
                            // 多媒体文件怎么可能是{开头,抛错吧
                            throw new IllegalArgumentException("mediaId="
                                                               + mediaId
                                                               + ","
                                                               + new String(data));
                        }
                    }
                    log.debugf("media download success mediaId=" + mediaId);
                    break;
                } else {
                    log.debugf("download %s fail, code=%s, content=%s",
                               mediaId,
                               resp.getStatus(),
                               resp.getContent());
                }
            }
            catch (Throwable e) {
                log.infof("download %s fail", mediaId, e);
            }
            finally {
                Streams.safeClose(in);
                Streams.safeClose(out);
            }
        }
        return mf;
    }

    // public static void main(String[] args) throws IOException {
    // for (Object obj : WxMsgType.values()) {
    // System.out.printf("WxOutMsg %s(WxInMsg msg);\n", obj);
    // }
    // for (Object obj : WxEventType.values()) {
    // System.out.printf("WxOutMsg event%s(WxInMsg msg);\n",
    // Strings.upperFirst(obj.toString().toLowerCase()));
    // }
    // StringWriter sw = new StringWriter();
    // asXml(sw, respText(null, "Hi"));
    // System.out.println(sw.toString());
    // }
}
