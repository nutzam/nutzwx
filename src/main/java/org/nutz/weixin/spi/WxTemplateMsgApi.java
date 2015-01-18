package org.nutz.weixin.spi;

import java.util.Map;

import org.nutz.weixin.bean.WxTemplateData;

public interface WxTemplateMsgApi {

    WxResp sendTemplateMsg(String touser, String template_id, String topcolor, Map<String, WxTemplateData> data);

}
