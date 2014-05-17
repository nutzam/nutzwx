package net.wendal.nutzwx.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.wendal.nutzwx.annotation.SimpleCURD;
import net.wendal.nutzwx.bean.User;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.resource.Scans;

@IocBean(create="init")
@At("/curd")
public class SimpleCURDModule {
	
	private static final Log log = Logs.get();

	@Inject
	protected Dao dao;
	
	protected Map<String, Class<?>> klassMap;
	
	public void init() {
		klassMap = new HashMap<>();
		for(Class<?> klass: Scans.me().scanPackage(User.class.getPackage().getName())){
			if (klass.getAnnotation(SimpleCURD.class) != null) {
				log.debug("load SimpleCURD marked class >> " + klass);
				klassMap.put(klass.getName().toLowerCase(), klass);
			}
		}
		log.debug("SimpleCURD marked class count="+klassMap.size());
	}
	
	@At("/add/?")
	public Object add(String klassName, Map<String, Object> map){
		return dao.insert(Lang.map2Object(map, beanClass(klassName)));
	}
	
	@At("/update/?")
	public int update(String klassName, Map<String, Object> map){
		return dao.update(Lang.map2Object(map, beanClass(klassName)));
	}
	
	@At("/delete/?")
	public int delete(String klassName, Map<String, Object> map){
		return dao.delete(Lang.map2Object(map, beanClass(klassName)));
	}
	
	@At("/query/?")
	public List<?> query(String klassName, Map<String, Object> map){
		Cnd cnd = Cnd.NEW();
		for (Entry<String, Object> en : map.entrySet()) {
			cnd.and(en.getKey(), "=", en.getValue());
		}
		return dao.query(beanClass(klassName), cnd);
	}
	
	@At("/clear/?")
	public int clear(String klassName, Map<String, Object> map) {
		if (map.isEmpty())
			throw new IllegalArgumentException("SimpleCURD.clear must HAVE Condition");
		Cnd cnd = Cnd.NEW();
		for (Entry<String, Object> en : map.entrySet()) {
			cnd.and(en.getKey(), "=", en.getValue());
		}
		return dao.clear(beanClass(klassName), cnd);
	}
	
	protected Class<?> beanClass(String klassName) {
		Class<?> klass = klassMap.get(klassName.toLowerCase());
		if (klass == null)
			throw new RuntimeException("not SimpleCURD class");
		return klass;
	}
}
