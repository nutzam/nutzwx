package org.nutz.dao.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.TableName;
import org.nutz.lang.Lang;
import org.nutz.trans.Molecule;

/**
 * 扩展Dao的功能
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class ExtDaos {
	
	private static Class<?>[] iz = new Class<?>[]{Dao.class};
	
	static {
		Proxy.getProxyClass(ExtDaos.class.getClassLoader(), iz);
	}
	
	/**
	 * 创建一个带FieldFilter的Dao代理实例. 注意,为避免出错,生成的Dao对象不应该传递到其他方法去.
	 * @param dao 原始的Dao实例
	 * @param filter 字段过滤器
	 * @return 带FieldFilter的Dao代理实例
	 */
	public static Dao ext(Dao dao, FieldFilter filter) {
		if (filter == null)
			return dao;
		ExtDaoInvocationHandler handler = new ExtDaoInvocationHandler(dao, filter, null);
		return (Dao) Proxy.newProxyInstance(dao.getClass().getClassLoader(), iz, handler);
	}
	
	public static Dao ext(Dao dao, Object tableName) {
		if (tableName == null)
			return dao;
		ExtDaoInvocationHandler handler = new ExtDaoInvocationHandler(dao, null, tableName);
		return (Dao) Proxy.newProxyInstance(dao.getClass().getClassLoader(), iz, handler);
	}
	
	public static Dao ext(Dao dao, FieldFilter filter, Object tableName) {
		if (tableName == null)
			return dao;
		ExtDaoInvocationHandler handler = new ExtDaoInvocationHandler(dao, filter, tableName);
		return (Dao) Proxy.newProxyInstance(dao.getClass().getClassLoader(), iz, handler);
	}
}
 
class ExtDaoInvocationHandler implements InvocationHandler {
	
	protected ExtDaoInvocationHandler(Dao dao, FieldFilter filter, Object tableName) {
		this.dao = dao;
		this.filter = filter;
		this.tableName = tableName;
	}
 
	protected Dao dao;
	protected FieldFilter filter;
	protected Object tableName;
 
	public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
		Molecule<Object> m = new ExtDaoMolecule(dao, method, args);
		if (filter != null)
			filter.run(m);
		else
			TableName.run(tableName, m);
		return m.getObj();
	}
	
}

class ExtDaoMolecule extends Molecule<Object> {
	protected Dao dao;
	protected Method method;
	protected Object args;
	
	public ExtDaoMolecule(Dao dao, Method method, Object args) {
		super();
		this.dao = dao;
		this.method = method;
		this.args = args;
	}
	
	public void run() {
		try {
			setObj(method.invoke(dao, args));
		}
		catch (IllegalArgumentException e) {
			throw Lang.wrapThrow(e);
		}
		catch (IllegalAccessException e) {
			throw Lang.wrapThrow(e);
		}
		catch (InvocationTargetException e) {
			throw Lang.wrapThrow(e.getTargetException());
		}
	}
}