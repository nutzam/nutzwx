package org.nutz.plugins.weixin;

import org.nutz.ioc.loader.json.JsonLoader;

public class WeixinIocLoader extends JsonLoader {

    public WeixinIocLoader() {
        super("org/nutz/plugins/weixin/weixin.js");
    }
}
