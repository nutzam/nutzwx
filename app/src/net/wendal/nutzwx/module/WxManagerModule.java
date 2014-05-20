package net.wendal.nutzwx.module;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.wendal.nutzwx.bean.WxMpInfo;
import net.wendal.nutzwx.service.NutDaoWxContext;
import net.wendal.nutzwx.util.Toolkit;

import org.keplerproject.luajava.LuaException;
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
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.view.HttpStatusView;
import org.nutz.weixin.spi.WxAPI;
import org.nutz.weixin.util.Wxs;

@At("/wx/manage")
@IocBean(depose="depose")
@Filters(@By(type=CheckSession.class, args={"usr", "/"}))
public class WxManagerModule {

	@Inject protected NutDaoWxContext wxctx;
	
	@At("/console/?")
	@Ok("jsp:jsp.usr.mp_console")
	@Fail("http:500")
	@GET
	public Object console(String openid, @Attr(value="usr", scope=Scope.SESSION)String usr){
		WxMpInfo master = (WxMpInfo) wxctx.get(openid);
		if (master == null || !usr.equals(master.getOwner())) {
			return new HttpStatusView(403);
		}
		return openid;
	}
	
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
			lua.pushJavaObject(Wxs.class);
			lua.setGlobal("wxs");
			// "wxs = luajava.bindClass('org.nutz.weixin.util.Wxs')\n"+ 
			int re = lua.LloadString(str);
			if (re != 0) {
				return "lua error re="+re + ", msg=" + lua.getLuaObject(1);
			}
			lua.pcall(0, 1, 0);
			if (re != 0) {
				return "lua error re="+re + ", msg=" + lua.getLuaObject(1);
			}
			return Toolkit.toJavaObject(lua.getLuaObject(1));
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
