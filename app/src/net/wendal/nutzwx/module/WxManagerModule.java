package net.wendal.nutzwx.module;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.wendal.nutzwx.bean.WxMpInfo;
import net.wendal.nutzwx.service.NutDaoWxContext;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaJavaAPI;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.mvc.Scope;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.view.HttpStatusView;
import org.nutz.weixin.spi.WxAPI;

@At("/wx/manage")
@IocBean(depose="depose")
@Filters(@By(type=CheckSession.class, args={"usr", "/"}))
public class WxManagerModule {

	@Inject protected NutDaoWxContext wxctx;
	
	@At("/console/?")
	@AdaptBy(type=VoidAdaptor.class)
	@POST
	@Ok("json")
	public Object console(String openid, @Attr(value="usr", scope=Scope.SESSION)String usr,  InputStream in) throws LuaException {
		WxMpInfo master = (WxMpInfo) wxctx.get(openid);
		if (master == null || !usr.equals(master.getOwner())) {
			return new HttpStatusView(403);
		}
		WxAPI wxapi = wxctx.getAPI(openid);
		LuaState lua = sandbox(openid);
		try {
			String str = Streams.readAndClose(new InputStreamReader(in));
			lua.pushJavaObject(wxapi);
			lua.setGlobal("wxapi");
			int re = lua.LloadString(str);
			lua.pcall(0, 1, 0);
			LuaObject luaobj = lua.getLuaObject(1);
			if (luaobj.isJavaObject()) {
				return luaobj.getObject();
			}
			if (luaobj.isJavaFunction()) {
				return "JavaFunction";
			}
			if (luaobj.isNil()) {
				return null;
			}
			if (luaobj.isNumber()) {
				return luaobj.getNumber();
			}
			if (luaobj.isBoolean()) {
				return luaobj.getBoolean();
			}
			if (luaobj.isString())
				return luaobj.getString();
			if (luaobj.isTable()) {
				return  "lua.table";
			}
			if (luaobj.isUserdata()) {
				return "lua.userdata";
			}
			if (luaobj.isFunction()) {
				return "lua.function";
			}
			return "re="+re;
		} finally {
			lua.close();
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
	
	public static void main(String[] args) throws LuaException {
		LuaState lua = LuaStateFactory.newLuaState();
		lua.openLibs();
		lua.pushJavaObject(new Thread() {
			public void run() {
				System.out.println("Lua Thread?");
			}
		});
		lua.setGlobal("t");
		int re = lua.LloadString("function f() t:run() return 123 end return f()");
		System.out.println(re);
		lua.pcall(0, 1, 0);
		System.out.println("re="+re);
		System.out.println(lua.getTop());
		System.out.println(lua.getLuaObject(1).getNumber());
		Lang.quiteSleep(2000);
//		System.out.println(System.currentTimeMillis());
	}
}
