package org.nutz.weixin.impl;

import java.util.List;
import java.util.Map;

import org.nutz.http.Request.METHOD;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.LoopException;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

public abstract class WxApi2Impl extends AbstractWxApi2 {

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
    
    public WxResp fetchUser(String openid, String lang) {
        return get("/user/info", "openid", openid, "lang", lang);
    }
    
    public WxResp userRemark(String openid, String remark) {
        return postJson("/user/info/updateremark", "openid", openid, "remark", remark);
    }
    
    @SuppressWarnings("unchecked")
    public void listWatcher(Each<String> each) {
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
}
