package net.wendal.base.mvc;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class SmartViewMaker implements ViewMaker {

	public View make(Ioc ioc, String type, String value) {
		if ("smart".equals(type) || "*".equals(type))
			return new SmartView(value);
		return null;
	}
}
