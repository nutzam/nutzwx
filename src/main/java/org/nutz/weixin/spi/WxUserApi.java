package org.nutz.weixin.spi;

import org.nutz.lang.Each;
import org.nutz.weixin.bean.WxGroup;

public interface WxUserApi {

    WxResp createGroup(WxGroup group);
    
    WxResp listGroup();
    
    WxResp userGroup(String openid);
    
    WxResp renameGroup(WxGroup group);
    
    WxResp moveUser2Group(String openid, String groupid);
    
    //-----------------------------------------------
    
    WxResp fetchUser(String openid, String lang);
    
    void listWatcher(Each<String> each);
    
    WxResp userRemark(String openid, String remark);
}
