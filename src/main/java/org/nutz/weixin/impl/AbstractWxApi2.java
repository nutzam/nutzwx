package org.nutz.weixin.impl;

import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

public abstract class AbstractWxApi2 implements WxApi2 {
    
    protected String token;
    protected String appid;
    protected String appsecret;
    protected String base = "https://api.weixin.qq.com/cgi-bin";
    protected String openid;

    protected String access_token;
    protected int access_token_expires;
    protected Object lock = new Object();

    public AbstractWxApi2() {}
    
    protected WxResp get(String uri, String ... args) {
        String params = "";
        for (int i = 0; i < args.length; i+=2) {
            if (args[i+1] != null)
                params += "&"+ args[i] + "=" + args[i+1];
        }
        return call(uri + "?_=1&" + params, METHOD.GET, null);
    }
    
    protected WxResp postJson(String uri, Object ... args) {
        NutMap body = new NutMap();
        for (int i = 0; i < args.length; i+=2) {
            body.put(args[i].toString(), args[i+1]);
        }
        return postJson(uri, body);
    }
    
    protected WxResp postJson(String uri, NutMap body) {
        return call(uri, METHOD.POST, Json.toJson(body));
    }

    protected WxResp call(String URL, METHOD method, String body) {
        String token = getAccessToken();
        if (URL.contains("?")) {
            URL = base + URL + "&access_token=" + token;
        } else {
            URL = base + URL + "?access_token=" + token;
        }
        Request req = Request.create(URL, method);
        if (body != null)
            req.setData(body);
        Response resp = Sender.create(req).send();
        if (!resp.isOK())
            throw new IllegalArgumentException("resp code=" + resp.getStatus());
        return Json.fromJson(WxResp.class, resp.getReader());
    }
    
    public String getAccessToken() {
        if (token == null || access_token_expires < System.currentTimeMillis() / 1000) {
            synchronized (lock) {
                if (token == null || access_token_expires < System.currentTimeMillis() / 1000) {
                    reflushAccessToken();
                    saveAccessToken(access_token, access_token_expires);
                }
            }
        }
        return token;
    }
    
    protected void reflushAccessToken() {
        String url = String.format("%s/token?grant_type=client_credential&appid=%s&secret=%s",
                                   base,
                                   appid,
                                   appsecret);
        Response resp = Http.get(url);
        if (!resp.isOK())
            throw new IllegalArgumentException("reflushAccessToken FAIL , openid=" + openid);
        String str = resp.getContent();
        NutMap re = Json.fromJson(NutMap.class, str);
        access_token = re.getString("access_token");
        access_token_expires = re.getInt("expires_in") - 60;// 提前一分钟
    }

    public void saveAccessToken(String token, long timeout) {}
}
