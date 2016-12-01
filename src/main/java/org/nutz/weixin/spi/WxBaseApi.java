package org.nutz.weixin.spi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;

/**
 *  @author wendal(wendal1985@gmail.com)
 */
public interface WxBaseApi {

    WxResp send(WxOutMsg out);
    
    WxInMsg parse(HttpServletRequest req);
    
    void handle(HttpServletRequest req, HttpServletResponse resp, WxHandler handler);
    
    List<String> getcallbackip();
}
