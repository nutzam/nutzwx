package org.nutz.weixin.session.memory;

import java.util.concurrent.ConcurrentHashMap;

import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.spi.WxSession;
import org.nutz.weixin.spi.WxSessionManager;

public class MemorySessionManager implements WxSessionManager {

	/**
	 * 默认超时为2天
	 */
	public static long DEF_TIMEOUT = 2*24*60;

	protected ConcurrentHashMap<String, MemoryWxSession> sessions = new ConcurrentHashMap<String, MemoryWxSession>();

	public WxSession getSession(WxInMsg in) {
		String sessionId = in.getFromUserName() + "@" + in.getToUserName();
		MemoryWxSession session = sessions.get(sessionId);
		if (session != null) {
			int maxInterval = session.getMaxInactiveInterval();
			if (maxInterval < 1) // 永不过期
				return session;
			long interval = (System.currentTimeMillis() - session.getLastAccessedTime()) / 1000 / 60;
			if (maxInterval > interval) {
				session.setLastAccessedTime(in.getCreateTime()*1000);
				return session;
			}
			session = null;
		}
		synchronized (sessions) {
			session = sessions.get(sessionId);
			if (session == null) {
				session = new MemoryWxSession(sessionId, this);
				session.setCreateTime(in.getCreateTime()*1000);
				session.setLastAccessedTime(in.getCreateTime()*1000);
				sessions.put(sessionId, session);
				return session;
			}
		}
		return session;
	}
	
	void remove(String id) {
		sessions.remove(id);
	}

}
