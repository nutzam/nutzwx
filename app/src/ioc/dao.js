var ioc = {
	config : {
		type : "org.nutz.ioc.impl.PropertiesProxy",
		fields : {
			paths : ["conf/"]
		}
	},
	druidStatFilter : {
		type : "com.alibaba.druid.filter.stat.MergeStatFilter",
		fields : {
			slowSqlMillis : 1000,
			logSlowSql : true
		}
	},
	dataSource : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		fields : {
			url : {java : "$config.get('jdbc.url')"},
			username : {java : "$config.get('jdbc.username')"},
			password : {java : "$config.get('jdbc.password')"},
			proxyFilters : [{refer:"druidStatFilter"}],
			removeAbandoned : true,
			removeAbandonedTimeout : 1800,
			logAbandoned : true,
			maxActive : 500
		},
		events : {
			create : "init",
			depose : "close"
		}
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [{refer:"dataSource"}]
	}
};