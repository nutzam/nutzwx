package org.nutz.weixin.at;

public class WxAccessToken {

	protected String access_token;
	protected int access_token_expires;
	
	public WxAccessToken() {
	}
	
	public WxAccessToken(String access_token, int access_token_expires) {
		super();
		this.access_token = access_token;
		this.access_token_expires = access_token_expires;
	}



	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getAccess_token_expires() {
		return access_token_expires;
	}
	public void setAccess_token_expires(int access_token_expires) {
		this.access_token_expires = access_token_expires;
	}
	
	
}
