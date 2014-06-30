package org.nutz.weixin.session.ssdb;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;
import org.nutz.weixin.spi.WxSession;

public class SsdbWxSession implements WxSession {
	
	protected SSDB ssdb;
	
	protected String id;
	
	public SsdbWxSession(SSDB ssdb, String id) {
		super();
		this.ssdb = ssdb;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public long getCreationTime() {
		return ssdb.hget(id, "ctime").check().asLong();
	}

	public Object getAttribute(String name) {
		Response resp = ssdb.hget(id, name);
		if (!resp.ok())
			return null;
		return resp.asString();
	}

	@Override
	public void setAttribute(String name, Object value) {
		ssdb.hset(id, name, value);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> keys = new HashSet<String>(ssdb.hkeys(id, "", "", -1).check().listString());
		keys.remove("ctime");
		keys.remove("latime");
		return Collections.enumeration(keys);
	}

	@Override
	public long getLastAccessedTime() {
		return ssdb.hget(id, "latime").check().asLong();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		// nop
	}

	@Override
	public int getMaxInactiveInterval() {
		// nop
		return 2*24*60*60;
	}

	@Override
	public void invalidate() {
		ssdb.hclear(id);
	}

}
