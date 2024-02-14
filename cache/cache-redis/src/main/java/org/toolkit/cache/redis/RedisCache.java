/* (C)2023*/
package org.toolkit.cache.redis;

import com.google.auto.factory.AutoFactory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@AutoFactory
public class RedisCache<K, V> {

    {
        RedisClient client = RedisClient.create(RedisURI.create("", 1));
        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(client::connect, new GenericObjectPoolConfig<>());
    }
    //	RedisTemplate<K, V> template;
}
