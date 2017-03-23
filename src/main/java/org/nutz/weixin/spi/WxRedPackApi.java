package org.nutz.weixin.spi;

import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxRedPack;
import org.nutz.weixin.bean.WxRedPackGroup;

import java.io.File;

/**
 * Created by wizzer on 2017/3/23.
 */
public interface WxRedPackApi {
    NutMap send_redpack(String key, WxRedPack wxRedPack, File file, String password);

    NutMap send_redpackgroup(String key, WxRedPackGroup wxRedPackGroup, File file, String password);
}
