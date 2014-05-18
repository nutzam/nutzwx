package net.wendal.nutzwx.module;

import java.io.InputStream;

import net.wendal.nutzwx.service.NutWxContext;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;

@At("/wx/manage")
@IocBean
public class WxManagerModule {

	@Inject protected NutWxContext wxctx;
	
	@At("/console/?")
	@AdaptBy(type=VoidAdaptor.class)
	@POST
	public Object console(String openid, InputStream in) {
		throw Lang.noImplement(); // Java内置的JS引擎,好难做sandbox,暂时搁置这个功能
	}
}
