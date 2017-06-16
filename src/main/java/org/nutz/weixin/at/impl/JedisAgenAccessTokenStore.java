package org.nutz.weixin.at.impl;

import java.util.HashMap;
import java.util.Map;

import org.nutz.integration.jedis.JedisAgent;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.weixin.at.WxAccessToken;
import org.nutz.weixin.spi.WxAccessTokenStore;

import redis.clients.jedis.Jedis;

public class JedisAgenAccessTokenStore implements WxAccessTokenStore {

    protected String tokenKey = "wxmp:access_token";

    protected JedisAgent jedisAgent;

    public JedisAgenAccessTokenStore(String tokenKey, JedisAgent jedisAgent) {
        if (!Strings.isBlank(tokenKey))
            this.tokenKey = tokenKey;
        this.jedisAgent = jedisAgent;
    }

    public WxAccessToken get() {
        Map<String, String> map;
        Jedis jedis = null;
        try {
            jedis = jedisAgent.getResource();
            map = jedis.hgetAll(tokenKey);
        } finally {
            Streams.safeClose(jedis);
        }
        if (map == null || map.isEmpty())
            return null;
        return Lang.map2Object(map, WxAccessToken.class);
    }

    public void save(String token, int expires, long lastCacheTimeMillis) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("expires", "" + expires);
        map.put("lastCacheTimeMillis", "" + lastCacheTimeMillis);
        Jedis jedis = null;
        try {
            jedis = jedisAgent.getResource();
            jedis.hmset(tokenKey, map);
        } finally {
            Streams.safeClose(jedis);
        }
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
