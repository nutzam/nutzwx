package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxPayTransfers;
import org.nutz.weixin.bean.WxPayTransfersInfo;
import org.nutz.weixin.bean.WxPayUnifiedOrder;

import java.io.File;

/**
 * Created by wizzer on 2017/3/23.
 */
public interface WxPayApi {
    NutMap pay_unifiedorder(String key, WxPayUnifiedOrder wxPayUnifiedOrder);

    NutMap pay_transfers(String key, WxPayTransfers wxPayTransfers, File file, String password);

    NutMap pay_transfersinfo(String key, WxPayTransfersInfo wxPayTransfersInfo, File file, String password);
}
