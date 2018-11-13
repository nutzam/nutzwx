package org.nutz.weixin.spi;

import java.util.List;

import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxMatchRule;
import org.nutz.weixin.bean.WxMenu;

/**
 * 
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public interface WxMenuApi {

    WxResp menu_create(NutMap map);

    WxResp menu_create(List<WxMenu> button);

    WxResp menu_get();

    WxResp menu_delete();

    WxResp menu_addconditional(List<WxMenu> button, WxMatchRule matchRule);

    WxResp menu_delconditional(String menuid);

    WxResp menu_trymatch(String user_id);

}
