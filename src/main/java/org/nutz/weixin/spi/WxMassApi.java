package org.nutz.weixin.spi;

import java.util.List;

import org.nutz.weixin.bean.WxArticle;
import org.nutz.weixin.bean.WxMsgType;
import org.nutz.weixin.bean.WxOutMsg;

/**
 * 高级群发
 * @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxMassApi {

    
    WxResp mass_uploadNews(List<WxArticle> news);
    
    WxResp mass_sendAll(boolean is_to_all, int group_id, boolean mpnews,  WxOutMsg msg);
    
    WxResp mass_send(List<String> touser, boolean mpnews,  WxOutMsg msg);
    
    WxResp mass_del(String msgid);
    
    WxResp mass_preview(String media_id, WxMsgType msgType, String touser);
    
    WxResp mass_get(String msgid);
    
}
