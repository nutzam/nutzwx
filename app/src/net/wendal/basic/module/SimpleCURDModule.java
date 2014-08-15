package net.wendal.basic.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.wendal.basic.bean.SimpleCurdConfig;
import net.wendal.basic.bean.User;
import net.wendal.nutzwx.annotation.SimpleCURD;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.segment.Segments;
import org.nutz.lang.util.Context;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.resource.Scans;
import org.nutz.trans.Atom;

@IocBean(create="init")
@At("/curd")
@Filters(@By(type=CheckSession.class, args={"usr", "/"}))
public class SimpleCURDModule {
	
	private static final Log log = Logs.get();

	@Inject
	protected Dao dao;
	
	protected Map<String, SimpleCurdConfig> klassMap;
	
	public void init() {
		klassMap = new HashMap<>();
		for(Class<?> klass: Scans.me().scanPackage(User.class.getPackage().getName())){
			SimpleCURD curd = klass.getAnnotation(SimpleCURD.class);
			if (curd != null) {
				log.debug("load SimpleCURD marked class >> " + klass);
				SimpleCurdConfig conf = new  SimpleCurdConfig(klass, curd.cnd());
				klassMap.put(klass.getSimpleName().toLowerCase(), conf);
			}
		}
		log.debug("SimpleCURD marked class count="+klassMap.size());
	}
	
	@At("/add/?")
	public Object add(String klassName, Map<String, Object> map){
		return dao.insert(Lang.map2Object(fixMap(get(klassName), map, base_ctx()), beanClass(klassName)));
	}
	
	@At("/update/?")
	public void update(final String klassName, final Map<String, Object> map){
		fixMap(get(klassName), map, base_ctx());
		FieldFilter.create(beanClass(klassName), "^("+Lang.concat("|", map.keySet()).toString() + ")$").run(new Atom() {
			public void run() {
				dao.update(Lang.map2Object(map, beanClass(klassName)));
			}
		});
	}
	
	@At("/delete/?")
	public int delete(String klassName, Map<String, Object> map){
		return dao.delete(Lang.map2Object(fixMap(get(klassName), map, base_ctx()), beanClass(klassName)));
	}
	
	@At("/query/?")
	public QueryResult query(String klassName, Map<String, Object> map, @Param("..")Pager pager){
		Cnd cnd = buildCnd(get(klassName), map, base_ctx());
		List<?> list = dao.query(beanClass(klassName), cnd, pager);
		QueryResult qr = new QueryResult(list, pager);
		pager.setRecordCount(dao.count(beanClass(klassName), cnd));
		return qr;
	}
	
	@At("/clear/?")
	public int clear(String klassName, Map<String, Object> map) {
		if (map.isEmpty())
			throw new IllegalArgumentException("SimpleCURD.clear must HAVE Condition");
		return dao.clear(beanClass(klassName), buildCnd(get(klassName), map, base_ctx()));
	}
	
	protected SimpleCurdConfig get(String klassName) {
		SimpleCurdConfig cnf = klassMap.get(klassName.toLowerCase());
		if (cnf == null) {
			throw new RuntimeException("not SimpleCURD class");
		}
		return cnf;
	}
	
	protected Class<?> beanClass(String klassName) {
		return get(klassName).getKlass();
	}
	
	protected Cnd buildCnd(SimpleCurdConfig cnf, Map<String, Object> map, Context ctx) {
		Cnd cnd = Cnd.NEW();
		if (cnf.getCnd().length > 1) {
			String[] _cnd_strs = cnf.getCnd();
			for (int i = 0; i < _cnd_strs.length; i+=3) {
				cnd.and(_cnd_strs[i], _cnd_strs[i+1], Segments.create(_cnd_strs[i+2]).render(ctx).toString());
			}
		}
		if (map != null && map.size() > 0) {
			SqlExpressionGroup sqlEG = new SqlExpressionGroup();
			for (Entry<String, Object> en : map.entrySet()) {
				sqlEG.and(en.getKey(), "=", en.getValue());
			}
			cnd.and(sqlEG);
		}
		return cnd;
	}
	
	protected Map<String, Object> fixMap(SimpleCurdConfig cnf, Map<String, Object> map, Context ctx) {
		if (cnf.getCnd().length > 1) {
			String[] _cnd_strs = cnf.getCnd();
			for (int i = 0; i < _cnd_strs.length; i+=3) {
				if ("=".equals(_cnd_strs[i+1]))
					map.put(_cnd_strs[i], Segments.create(_cnd_strs[i+2]).render(ctx).toString());
			}
		}
		return map;
	}
	
	protected Context base_ctx() {
		return Lang.context().set("usr", Mvcs.getHttpSession().getAttribute("usr"));
	}
}
