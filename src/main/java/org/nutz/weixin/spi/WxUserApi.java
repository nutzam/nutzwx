package org.nutz.weixin.spi;

import org.nutz.lang.Each;
import org.nutz.weixin.bean.WxGroup;

/**
 *  用户管理API
 * @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxUserApi {

    WxResp groups_create(WxGroup group);
    
    WxResp groups_get();
    
    WxResp groups_getid(String openid);
    
    WxResp groups_update(WxGroup group);
    
    WxResp groups_member_update(String openid, String groupid);
    
    //-----------------------------------------------
    
    WxResp user_info(String openid, String lang);
    
    void user_get(Each<String> each);
    
    WxResp user_info_updatemark(String openid, String remark);
}
