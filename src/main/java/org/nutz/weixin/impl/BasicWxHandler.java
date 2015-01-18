package org.nutz.weixin.impl;

import org.nutz.weixin.util.Wxs;

public class BasicWxHandler extends AbstractWxHandler {
	
	protected String token;
	
	protected BasicWxHandler() {}
	
	public BasicWxHandler(String token) {
		this.token = token;
	}

	public boolean check(String signature, String timestamp, String nonce, String key) {
		return Wxs.check(token, signature, timestamp, nonce);
	}

}
