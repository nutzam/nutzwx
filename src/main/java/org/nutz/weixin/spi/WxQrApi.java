package org.nutz.weixin.spi;

/**
 * 
 *  @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxQrApi {
    
    WxResp qrcode_create(Object scene_id, int expire_seconds);
    
    String qrcode_show(String ticket);
    
}
