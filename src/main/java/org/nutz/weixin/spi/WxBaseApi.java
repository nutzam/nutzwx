package org.nutz.weixin.spi;

import org.nutz.weixin.bean.WxOutMsg;

/**
 *  @author wendal(wendal1985@gmail.com)
 */
public interface WxBaseApi {

    WxResp send(WxOutMsg out);  

    String getAccessToken();

    void saveAccessToken(String token, long timeout);
}
