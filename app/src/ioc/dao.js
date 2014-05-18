var ioc = {
	config : {
		type : "org.nutz.ioc.impl.PropertiesProxy",
		fields : {
			paths : ["conf/"]
		}
	},
	dataSource : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		fields : {
			url : {java : "$config.get('jdbc.url')"},
			username : {java : "$config.get('jdbc.username')"},
			password : {java : "$config.get('jdbc.password')"},
		},
		events : {
			depose : "close"
		}
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [{refer:"dataSource"}]
	}
};