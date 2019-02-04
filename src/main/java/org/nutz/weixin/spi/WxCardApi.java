package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;

/**
 * 
 * @author JinYi(wdhlzd@163.com)
 * 
 */
public interface WxCardApi {

    /**
     * 微信卡券：创建卡券
     *
     * @author JinYi
     * @param card
     * @return
     */
    WxResp card_create(NutMap card);

    /**
     * 微信卡券：投放卡券，创建二维码
     *
     * @author JinYi
     * @param body
     * @return
     */
    WxResp card_qrcode_create(NutMap body);

    /**
     * 微信卡券：查询Code
     *
     * @author JinYi
     * @param code 单张卡券的唯一标准，必填
     * @param cardId 卡券ID代表一类卡券，null表示不填此参数。自定义code卡券必填
     * @param checkConsume 是否校验code核销状态，填入true和false时的code异常状态返回数据不同，null表示不填此参数
     * @return
     */
    WxResp card_code_get(String code, String cardId, Boolean checkConsume);

    /**
     * 微信卡券：查询Code
     *
     * @author JinYi
     * @param code 单张卡券的唯一标准，必填
     * @param cardId 卡券ID代表一类卡券，null表示不填此参数。自定义code卡券必填
     * @return
     */
    WxResp card_code_get(String code, String cardId);

    /**
     * 微信卡券：查询Code
     *
     * @author JinYi
     * @param code 单张卡券的唯一标准，必填
     * @return
     */
    WxResp card_code_get(String code);
}
