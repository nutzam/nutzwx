package org.nutz.weixin.impl;

import org.nutz.weixin.repo.com.qq.weixin.mp.aes.AesException;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.nutz.weixin.util.Wxs;

public class BasicWxHandler extends AbstractWxHandler {
	
	protected String token;
	
	protected String aesKey;
	
	protected WXBizMsgCrypt msgCrypt;
	
	protected String appId;
	
	protected BasicWxHandler() {}
    
    public BasicWxHandler(String token) {
        this.token = token;
    }

	public BasicWxHandler(String token, String aesKey, String appId) {
        super();
        this.token = token;
        this.aesKey = aesKey;
        this.appId = appId;
    }

    public boolean check(String signature, String timestamp, String nonce, String key) {
		return Wxs.check(token, signature, timestamp, nonce);
	}
	
	public WXBizMsgCrypt getMsgCrypt() {
	    if (msgCrypt == null)
            try {
                msgCrypt = new WXBizMsgCrypt(token, aesKey, appId);
            }
            catch (AesException e) {
                throw new RuntimeException(e);
            }
	    return msgCrypt;
	}
}
