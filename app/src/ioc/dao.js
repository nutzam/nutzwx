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
			url : {java : "$config.get('url')"},
			username : {java : "$config.get('username')"},
			password : {java : "$config.get('password')"},
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