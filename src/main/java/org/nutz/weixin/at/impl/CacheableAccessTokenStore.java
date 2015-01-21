package org.nutz.weixin.at.impl;

import org.nutz.weixin.at.WxAccessToken;
import org.nutz.weixin.spi.WxAccessTokenStore;

public abstract class CacheableAccessTokenStore implements WxAccessTokenStore {

	protected int timeout;
	protected WxAccessToken at;
	protected Object lock = new Object();

	public WxAccessToken getAccessToken() {
		if (timeout > 0) {
			synchronized (lock) {
				if (timeout > 0 && at != null && (System.currentTimeMillis()/1000 - at.getAccess_token_expires()) < timeout)
					return at;
			}
		}
		WxAccessToken tmp = _getAccessToken();
		if (timeout > 0) {
			synchronized (lock) {
				at = tmp;
			}
		}
		return tmp;
	}
	
	protected abstract WxAccessToken _getAccessToken();
	protected abstract void _saveAccessToken(String token, int time);

	public void saveAccessToken(String token, int time) {
		_saveAccessToken(token, time);
		if (time > 0) {
			synchronized (lock) {
				at = null;
				getAccessToken();
			}
		}
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
