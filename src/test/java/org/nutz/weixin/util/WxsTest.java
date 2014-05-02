package org.nutz.weixin.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class WxsTest {

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

}
