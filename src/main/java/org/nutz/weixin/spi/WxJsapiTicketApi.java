package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;

public interface WxJsapiTicketApi {

    void setJsapiTicketStore(WxJsapiTicketStore ats);

    WxJsapiTicketStore getJsapiTicketStore();

    String getJsapiTicket();

    NutMap genJsSDKConfig(String url, String... jsApiList);

}
