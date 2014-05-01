package org.nutz.weixin.mvc;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Lang;
import org.nutz.mvc.adaptor.AbstractAdaptor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.util.Wxs;

public class WxAdaptor extends AbstractAdaptor {

	protected ParamInjector evalInjectorBy(Type type, Param arg1) {
		Class<?> clazz = Lang.getTypeClass(type);
		if (!clazz.isAssignableFrom(WxInMsg.class))
			return null;
		return new ParamInjector() {

			public Object get(ServletContext sc, HttpServletRequest req, HttpServletResponse resp, Object obj) {
				// 校验签名
				if ("GET".equalsIgnoreCase(req.getMethod()))
					return null;
				try {
					return Wxs.convert(req.getInputStream());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

}
