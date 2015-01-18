package org.nutz.weixin.spi;

import org.nutz.weixin.bean.WxMenu;

public interface WxMenuApi {

    WxResp creatMenu(WxMenu menu);
    
    WxResp fetchMenu();
    
    WxResp clearMenu();

}
