package org.nutz.weixin.spi;

import java.io.File;

public interface WxMediaApi {

    WxResp mediaUpload(String type, File f);
    
    WxResp mediaGet(String mediaId);

}
