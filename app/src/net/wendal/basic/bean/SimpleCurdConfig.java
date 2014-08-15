package net.wendal.basic.bean;

public class SimpleCurdConfig {

	private Class<?> klass;
	private String[] cnd;
	
	public SimpleCurdConfig() {
	}
	
	public SimpleCurdConfig(Class<?> klass, String[] cnd) {
		super();
		this.klass = klass;
		this.cnd = cnd;
	}

	public Class<?> getKlass() {
		return klass;
	}
	public void setKlass(Class<?> klass) {
		this.klass = klass;
	}
	public String[] getCnd() {
		return cnd;
	}
	public void setCnd(String[] cnd) {
		this.cnd = cnd;
	}
	
	
}
