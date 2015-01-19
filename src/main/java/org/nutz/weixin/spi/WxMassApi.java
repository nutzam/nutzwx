package org.nutz.weixin.spi;

import java.util.List;

import org.nutz.weixin.bean.WxArticle;
import org.nutz.weixin.bean.WxOutMsg;

/**
 * 高级群发
 *  @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxMassApi {

    
    WxResp mass_uploadnews(List<WxArticle> articles);
    
    WxResp mass_sendall(boolean is_to_all, String group_id, WxOutMsg msg);
    
    WxResp mass_send(List<String> touser, WxOutMsg msg);
    
    WxResp mass_del(String msg_id);
    
    WxResp mass_preview(String touser, WxOutMsg msg);
    
    WxResp mass_get(String msg_id);
    
}
