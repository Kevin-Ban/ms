package com.gateway.bean;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

public class RedisRateLimitScript implements RedisScript<String> {
    private static final String SCRIPT =
            "local key = KEYS[1] " +
                    " local limit = tonumber(ARGV[1]) " +
                    " local current = tonumber(redis.call('get', key) or '0')" +
                    " if current + 1 > limit " +
                    " then  return 0 " +
                    " else " +
                    " redis.call('INCRBY', key,'1')" +
                    " redis.call('expire', key,'2') " +
                    " end return 1 ";

    @Override
    public String getSha1() {
        return DigestUtils.sha1Hex(SCRIPT);
    }

    @Override
    public Class<String> getResultType() {
        return String.class;
    }

    @Override
    public String getScriptAsString() {
        return SCRIPT;
    }
}
