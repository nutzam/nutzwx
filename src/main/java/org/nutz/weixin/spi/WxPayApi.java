package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.*;

import java.io.File;
import java.util.Map;

/**
 * Created by wizzer on 2017/3/23.
 */
public interface WxPayApi {
    NutMap postPay(String url, String key, Map<String, Object> params);

    NutMap postPay(String url, String key, Map<String, Object> params, File file, String password);

    NutMap pay_unifiedorder(String key, WxPayUnifiedOrder wxPayUnifiedOrder);

    NutMap pay_jsapi(String key, WxPayUnifiedOrder wxPayUnifiedOrder);

    NutMap pay_transfers(String key, WxPayTransfers wxPayTransfers, File file, String password);

    NutMap send_redpack(String key, WxPayRedPack wxRedPack, File file, String password);

    NutMap send_redpackgroup(String key, WxPayRedPackGroup wxRedPackGroup, File file, String password);

    NutMap send_coupon(String key, WxPayCoupon wxPayCoupon, File file, String password);
}
