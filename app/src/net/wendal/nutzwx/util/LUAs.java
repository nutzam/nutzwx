package net.wendal.nutzwx.util;

import net.wendal.base.util.Toolkit;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.nutz.lang.Lang;

public class LUAs {

	public static Object toJavaObject(LuaObject luaobj) throws LuaException {
		if (luaobj.isJavaObject()) {
			Object obj = luaobj.getObject();
			if (obj instanceof Throwable)
				Toolkit.log.info("lua Err?", (Throwable)obj);
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
		throw Lang.noImplement();
	}

}
