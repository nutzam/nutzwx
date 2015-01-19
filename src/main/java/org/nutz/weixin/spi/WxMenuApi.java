package org.nutz.weixin.spi;

import java.awt.Menu;
import java.util.List;

/**
 * 
 *  @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxMenuApi {

    WxResp menu_create(List<Menu> button);
    
    WxResp menu_get();
    
    WxResp menu_delete();

}
