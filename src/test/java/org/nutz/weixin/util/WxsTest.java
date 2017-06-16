package org.nutz.weixin.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nutz.ioc.ObjectProxy;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.spi.WxApi2;

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

	
	@Test
	public void test_ioc_load() throws ClassNotFoundException {
	    // 仿真一个配置文件
        PropertiesProxy conf = new PropertiesProxy();
        conf.put("weixin.atstore", "jedis"); // 测试jedisAgent存储AccessToken
        conf.put("weixin.token", "1234567890");
        conf.put("weixin.appid", "wx10927e35a365fe1c");
        conf.put("weixin.appsecret", "c29accd1784e636d6478eac9b6b3aYYY"); // YYY是假的
        conf.put("weixin.openid", "XXX");
        
        // 沙箱测试
	    // http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
	    NutIoc ioc = new NutIoc(new ComboIocLoader("*weixin", "*jedis"));
	    ioc.getIocContext().save("app", "conf", new ObjectProxy(conf));
	    
	    WxApi2 wxApi2 = ioc.get(WxApi2.class);
	    for (int i = 0; i < 6; i++) {
	        wxApi2.get_industry();
        }
	    ioc.depose();
	}
}
