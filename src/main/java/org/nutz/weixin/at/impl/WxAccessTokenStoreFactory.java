package org.nutz.weixin.at.impl;

import org.nutz.dao.Dao;
import org.nutz.integration.jedis.JedisAgent;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;
import org.nutz.weixin.spi.WxAccessTokenStore;

import redis.clients.jedis.JedisPool;

public class WxAccessTokenStoreFactory {

    public static WxAccessTokenStore make(String type, Ioc ioc) {
        if (Strings.isBlank(type) || "memory".equals(type)) {
            return new MemoryAccessTokenStore();
        }
        if ("dao".equals(type)) {
            return new DaoAccessTokenStore(ioc.get(Dao.class));
        }
        if ("jedisPool".equals(type)) {
            PropertiesProxy conf = ioc.get(PropertiesProxy.class, "conf");
            return new RedisAccessTokenStore(conf.get("weixin.redis.key"),
                                             ioc.get(JedisPool.class));
        }
        if ("jedis".equals(type)) {
            PropertiesProxy conf = ioc.get(PropertiesProxy.class, "conf");
            return new JedisAgenAccessTokenStore(conf.get("weixin.redis.key"),
                                                 ioc.get(JedisAgent.class));
        }
        throw new RuntimeException("unsupport type=" + type);
    }
}
