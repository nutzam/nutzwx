package net.wendal.nutzwx.module;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.wendal.nutzwx.service.NutDaoWxContext;

import org.keplerproject.luajava.LuaJavaAPI;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Streams;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.weixin.util.WxLua;

@At("/wx/manage")
@IocBean(depose="depose")
public class WxManagerModule {

	@Inject protected NutDaoWxContext wxctx;
	
	@At("/console/?")
	@AdaptBy(type=VoidAdaptor.class)
	@POST
	public Object console(String openid, InputStream in) {
		LuaState lua = sandbox(openid);
		WxLua.wxapi.set(wxctx.getAPI(openid));
		try {
			String str = Streams.readAndClose(new InputStreamReader(in));
			str = "wx = luajava.bindClass('org.nutz.weixin.util.WxLua')\n" + str;
			int re = lua.LloadString(str);
			lua.pcall(0, 0, 0);
			return "re="+re;
		} finally {
			lua.close();
			WxLua.wxapi.set(null);
		}
	}
	
	public LuaState sandbox(String openid) {
		LuaState lua = LuaStateFactory.newLuaState();
		
		lua.openBase();
		lua.openDebug();
		lua.openMath();
		lua.openString();
		lua.openTable();
		
		// 移除loadfile方法
		lua.pushNil();
		lua.setGlobal("loadfile");
		
		
		
		return lua;
	}
	
	public void depose() {
		LuaJavaAPI.sandbox = null;
	}
	
	public static void main(String[] args) {
		System.out.println("java.library.path=" + System.getProperty("java.library.path"));
		LuaState lua = LuaStateFactory.newLuaState();
		lua.openLibs();
		int re = lua.LloadString("print('ABC'); print(_VERSION);");
		lua.pcall(0, 0, 0);
		System.out.println("re="+re);
	}
}
