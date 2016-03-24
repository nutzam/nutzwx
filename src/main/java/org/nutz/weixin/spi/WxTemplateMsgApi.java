package org.nutz.weixin.spi;

import java.util.Map;

import org.nutz.weixin.bean.WxTemplateData;

/**
 * 模块消息API
 * 
 * @author wendal(wendal1985@gmail.com)
 *
 */
public interface WxTemplateMsgApi {

	WxResp template_api_set_industry(String industry_id1, String industry_id2);

	WxResp template_api_add_template(String template_id_short);

	WxResp template_send(String touser, String template_id, String url, Map<String, WxTemplateData> data);

	WxResp get_all_private_template();

	WxResp get_industry();

}
