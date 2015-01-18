package org.nutz.weixin.spi;

public interface WxQrApi {
    
    WxResp tmpQr(int expire_seconds, String scene_id);
    
    WxResp godQr(int scene_id);
    
    WxResp qrUrl(String ticket);
    

}
