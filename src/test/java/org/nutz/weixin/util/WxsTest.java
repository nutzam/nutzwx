package org.nutz.weixin.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class WxsTest {

	private static final Log log = Logs.get();
	
	@Test
	public void testCheck() {
		Wxs.enableDevMode();
		String signature="60a204faed3330f299c2aafe2d3afecd253a59ec";
		//String echostr="2814757190284582366";
		String timestamp="1399036062";
		String nonce="417048531";
		String token = "9b6b3a8ae";
		assertTrue(Wxs.check(token, signature, timestamp, nonce));
	}
	
	@Test
	public void testLogs(){
		log.infof("The nutzwx project is developed by %s %s %s", "112", "1212", null);
	}

}
