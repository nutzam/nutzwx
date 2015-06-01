package org.nutz.weixin.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.castor.Castors;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.http.sender.FilePostSender;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.resource.NutResource;
import org.nutz.weixin.bean.WxArticle;
import org.nutz.weixin.bean.WxGroup;
import org.nutz.weixin.bean.WxMenu;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.bean.WxTemplateData;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

public class WxApi2Impl extends AbstractWxApi2 {

    private static final Log log = Logs.get().setTag("weixin");

    public WxApi2Impl() {}

    // ===============================
    // 基本API

    public WxResp send(WxOutMsg out) {
        if (out.getFromUserName() == null)
            out.setFromUserName(openid);
        String str = Wxs.asJson(out);
        if (Wxs.DEV_MODE)
            log.debug("api out msg>\n" + str);
        return call("/message/custom/send", METHOD.POST, str);
    }

    // -------------------------------
    // 用户API

    public WxResp user_info(String openid, String lang) {
        return get("/user/info", "openid", openid, "lang", lang);
    }

    public WxResp user_info_updatemark(String openid, String remark) {
        return postJson("/user/info/updateremark", "openid", openid, "remark", remark);
    }

    @SuppressWarnings("unchecked")
    public void user_get(Each<String> each) {
        String next_openid = null;
        WxResp map = null;
        int count = 0;
        int total = 0;
        int index = 0;
        while (true) {
            if (next_openid == null)
                map = call("/user/get", METHOD.GET, null);
            else
                map = call("/user/get?next_openid=" + next_openid, METHOD.GET, null);
            count = ((Number) map.get("count")).intValue();
            if (count < 1)
                return;
            total = ((Number) map.get("total")).intValue();
            next_openid = Strings.sNull(map.get("next_openid"));
            if (next_openid.length() == 0)
                next_openid = null;
            List<String> openids = (List<String>) ((Map<String, Object>) map.get("data")).get("openid");
            for (String openid : openids) {
                try {
                    each.invoke(index, openid, total);
                }
                catch (ExitLoop e) {
                    return;
                }
                catch (ContinueLoop e) {
                    continue;
                }
                catch (LoopException e) {
                    throw e;
                }
                index++;
            }
        }
    }

    public WxResp groups_create(WxGroup group) {
        return postJson("/groups/create", "group", group);
    }

    public WxResp groups_get() {
        return call("/groups/get", METHOD.GET, null);
    }

    @Override
    public WxResp groups_getid(String openid) {
        return postJson("/groups/getid", "openid", openid);
    }

    @Override
    public WxResp groups_update(WxGroup group) {
        return postJson("/groups/update", "group", group);
    }

    @Override
    public WxResp groups_member_update(String openid, String to_groupid) {
        return postJson("/groups/member/update", "openid", openid, "to_groupid", to_groupid);
    }

    // -------------------------------------------------------
    // 二维码API

    public WxResp qrcode_create(Object scene_id, int expire_seconds) {
        NutMap params = new NutMap();
        NutMap scene;
        // 临时二维码
        if (expire_seconds > 0) {
            params.put("action_name", "QR_SCENE");
            params.put("expire_seconds", expire_seconds);

            scene = Lang.map("scene_id", Castors.me().castTo(scene_id, Integer.class));
        }
        // 永久二维码
        else if (scene_id instanceof Number) {
            params.put("action_name", "QR_LIMIT_SCENE");
            scene = Lang.map("scene_id", Castors.me().castTo(scene_id, Integer.class));
        }
        // 永久字符串二维码
        else {
            params.put("action_name", "QR_LIMIT_STR_SCENE");
            scene = Lang.map("scene_str", scene_id.toString());
        }
        params.put("action_info", Lang.map("scene", scene));
        return postJson("/qrcode/create", params);
    }

    public String qrcode_show(String ticket) {
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
    }

    // --------------------------------------------------------
    // 模板消息

    public WxResp template_api_set_industry(String industry_id1, String industry_id2) {
        return postJson("/template/api_set_industry",
                        "industry_id1",
                        industry_id1,
                        "industry_id2",
                        industry_id2);
    }

    public WxResp template_api_add_template(String template_id_short) {
        return postJson("/template/api_add_template", "template_id_short", template_id_short);
    }

    @Override
    public WxResp template_send(String touser,
                                String template_id,
                                String topcolor,
                                Map<String, WxTemplateData> data) {
        return postJson("/template/send",
                        "touser",
                        touser,
                        "template_id",
                        template_id,
                        "topcolor",
                        topcolor,
                        "data",
                        data);
    }

    // ------------------------------------------------------------
    // 自定义菜单

    public WxResp menu_create(NutMap map) {
        return postJson("/menu/create", map);
    }

    public WxResp menu_create(List<WxMenu> button) {
        return postJson("/menu/create", "button", button);
    }

    public WxResp menu_get() {
        return call("/menu/get", METHOD.GET, null);
    }

    public WxResp menu_delete() {
        return call("/menu/delete", METHOD.GET, null);
    }

    // 多媒体上传下载

    public WxResp media_upload(String type, File f) {
        if (type == null)
            throw new NullPointerException("media type is NULL");
        if (f == null)
            throw new NullPointerException("meida file is NULL");
        String url = String.format("http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s",
                                   getAccessToken(),
                                   type);
        Request req = Request.create(url, METHOD.POST);
        req.getParams().put("media", f);
        Response resp = new FilePostSender(req).send();
        if (!resp.isOK())
            throw new IllegalStateException("media upload file, resp code=" + resp.getStatus());
        return null;
    }

    public NutResource media_get(String mediaId) {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("token", getAccessToken());
        params.put("media_id", mediaId);
        final Response resp = Sender.create(Request.create(url, METHOD.GET)).send();
        if (!resp.isOK())
            throw new IllegalStateException("download media file, resp code=" + resp.getStatus());
        final String disposition = resp.getHeader().get("Content-disposition");
        return new NutResource() {

            public String getName() {
                if (disposition == null)
                    return "file.data";
                for (String str : disposition.split(";")) {
                    if (str.startsWith("filename=")) {
                        str = str.substring("filename=".length());
                        if (str.startsWith("\""))
                            str = str.substring(1);
                        if (str.endsWith("\""))
                            str = str.substring(0, str.length() - 1);
                        return str.trim().intern();
                    }
                }
                return "file.data";
            }

            public InputStream getInputStream() throws IOException {
                return resp.getStream();
            }
        };
    }

    // 高级群发
    public WxResp mass_uploadnews(List<WxArticle> articles) {
        return postJson("/message/mass/uploadnews", "articles", articles);
    }

    public WxResp _mass_send(NutMap filter, List<String> to_user, String touser, WxOutMsg msg) {
        NutMap params = new NutMap();
        if (filter != null)
            params.setv("filter", filter);
        else if (to_user != null) {
            params.setv("to_user", to_user);
        } else {
            params.put("touser", touser);
        }
        if ("text".equals(msg.getMsgType())) {
            params.put("text", new NutMap().setv("content", msg.getContent()));
        } else {
            params.put(msg.getMsgType(), new NutMap().setv("media_id", msg.getMedia_id()));
            // TODO title 和 description, thumb_media_id
        }
        params.setv("msgtype", msg.getMsgType());

        if (filter != null)
            return postJson("/message/mass/sendall", params);
        else if (to_user != null)
            return postJson("/message/mass/send", params);
        return postJson("/message/mass/preview", params);
    }

    public WxResp mass_sendall(boolean is_to_all, String group_id, WxOutMsg msg) {
        NutMap filter = new NutMap();
        filter.put("is_to_all", is_to_all);
        if (!is_to_all) {
            filter.put("group_id", group_id);
        }
        return this._mass_send(filter, null, null, msg);
    }

    public WxResp mass_send(List<String> to_user, WxOutMsg msg) {
        return this._mass_send(null, to_user, null, msg);
    }

    public WxResp mass_del(String msg_id) {
        return this.postJson("/message/mass/del", "msg_id", msg_id);
    }

    public WxResp mass_get(String msg_id) {
        return postJson("/message/mass/get", "msg_id", msg_id);
    }

    public WxResp mass_preview(String touser, WxOutMsg msg) {
        return _mass_send(null, null, touser, msg);
    }
}
