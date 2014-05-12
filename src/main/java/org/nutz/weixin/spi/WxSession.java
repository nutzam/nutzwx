package org.nutz.weixin.spi;

import java.util.Enumeration;

public interface WxSession {

	public String getId();
	public long getCreationTime();
	public Object getAttribute(String name);
	public void setAttribute(String name, Object value);
	public Enumeration<String> getAttributeNames();
	public long getLastAccessedTime();
	
	public void setMaxInactiveInterval(int interval);
	public int getMaxInactiveInterval();
	
	public void invalidate();
}
