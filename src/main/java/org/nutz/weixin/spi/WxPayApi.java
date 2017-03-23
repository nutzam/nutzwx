package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxPayUnifiedOrder;

/**
 * Created by wizzer on 2017/3/23.
 */
public interface WxPayApi {
    NutMap pay_unifiedorder(String key, WxPayUnifiedOrder wxPayUnifiedOrder);
}
