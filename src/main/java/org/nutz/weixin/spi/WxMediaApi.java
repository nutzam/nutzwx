package org.nutz.weixin.spi;

import java.io.File;
import java.io.InputStream;

import org.nutz.resource.NutResource;

/**
 * 
 *  @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxMediaApi {

    WxResp media_upload(String type, File f);

    WxResp media_upload(String type, InputStream inputStream);
    
    NutResource media_get(String mediaId);

}
