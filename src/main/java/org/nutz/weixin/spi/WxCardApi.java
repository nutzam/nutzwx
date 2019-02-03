package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;

/**
 * 
 * @author JinYi(wdhlzd@163.com)
 * 
 */
public interface WxCardApi {

    /**
     * 创建卡券
     *
     * @author JinYi
     * @param card
     * @return
     */
    WxResp card_create(NutMap card);

}
