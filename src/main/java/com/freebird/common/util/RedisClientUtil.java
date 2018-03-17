package com.freebird.common.util;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

public class RedisClientUtil {

	private static volatile JedisPool pool = null;

	private static Object lock = new Object();

	private static boolean usePool = false;

	private static int maxIdle = 0;

	private static int maxTotal = 0;

	private static int minIdle = 0;

	private static String jedisIp = null;

	private static int jedisPort = 0;

	private static String jedisPassword = null;

	private static boolean testOnBorrow = false;

	private static int jedisTimeout = 0;

	static {
		String redisIp = PropertiesUtils.getPropertyValues("redis.ip");
		String redisPort = PropertiesUtils.getPropertyValues("redis.port");
		String redisTimeOut = PropertiesUtils.getPropertyValues("redis.timeout");
		String maxIdle_ = PropertiesUtils.getPropertyValues("redis.maxIdle");
		String maxTotal_ = PropertiesUtils.getPropertyValues("redis.maxTotal");
		String minIdle_ = PropertiesUtils.getPropertyValues("redis.minIdle");
		String usePool_ = PropertiesUtils.getPropertyValues("redis.usePool");
		String testOnBorrow_ = PropertiesUtils.getPropertyValues("redis.testOnBorrow");

		testOnBorrow_ = StringUtils.isBlank(testOnBorrow_) ? "false" : testOnBorrow_;
		int port = (new Integer(redisPort)).intValue();
		int timeOut = (new Integer(redisTimeOut)).intValue();

		testOnBorrow = (new Boolean(testOnBorrow_)).booleanValue();
		usePool = "true".equals(usePool_); // 是否使用Pool
		maxIdle = Integer.parseInt(maxIdle_); // 最大等待连接数
		maxTotal = Integer.parseInt(maxTotal_); // 最大连接数
		minIdle = Integer.parseInt(minIdle_); // 最小等待连接数
		jedisIp = redisIp; //连接IP
		jedisPort = port; //连接端口
		jedisTimeout = timeOut; // 连接超时时间，毫秒
		jedisPassword = PropertiesUtils.getPropertyValues("redis.password");

	}

	private static JedisPool getJedisPool() {
		if (pool == null) {
			synchronized (lock) {
				if (pool == null) {
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxIdle(maxIdle);
					config.setMaxTotal(maxTotal);
					config.setMinIdle(minIdle);
					config.setTestOnBorrow(testOnBorrow);
					//JedisPool newPool = new JedisPool(config, jedisIp, jedisPort, jedisTimeout, jedisPassword);
					JedisPool newPool = new JedisPool(config, jedisIp, jedisPort, jedisTimeout);
					// JedisPool newPool = new JedisPool(jedisIp, jedisPort);
					pool = newPool;
				}
			}
		}
		return pool;
	}

	private static Jedis getJedis() {
		if (usePool) {
			return getJedisPool().getResource();
		} else {
			Jedis p = new Jedis(jedisIp, jedisPort);
//			p.auth(jedisPassword);
			return p;
		}
	}

	/**
	 * 私有方法:关闭/释放Jedis连接
	 * @param key 键
	 * @param value 值
	 * @throws JedisException
	 */
	private static void closeJedis(Jedis p) {
		if (p != null) {
			if (usePool) {
				getJedisPool().returnResourceObject(p);
			} else {
				p.disconnect();
			}
		}
	}

	/**
	 * 公共方法：存String值，有过期时间
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 数据有效期 -1是永久有效
	 * @throws JedisException
	 */
	public static void setString(String key, String value, int cacheSeconds) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
			if (cacheSeconds >= 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：存String值，无过期时间
	 * @param key 键
	 * @param value 值
	 * @throws JedisException
	 */
	public static void setString(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：存String值，无过期时间
	 * @param key 键
	 * @param value 值
	 * @throws JedisException
	 */
	public static void setString(byte[] key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：存String值，有过期时间
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 数据有效期 -1是永久有效
	 * @throws JedisException
	 */
	public static void setString(byte[] key, byte[] value, int cacheSeconds) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
			if (cacheSeconds >= 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：取String值
	 * @param key 键
	 * @throws JedisException
	 */
	public static String getString(String key) {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.get(key);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：取String值
	 * @param key 键
	 * @throws JedisException
	 */
	public static byte[] getString(byte[] key) {
		Jedis jedis = null;
		byte[] result = null;
		try {
			jedis = getJedis();
			result = jedis.get(key);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：获取Hash值
	 * @param key fields
	 * @return List<String>
	 * @throws JedisException
	 */
	public static List<String> hmget(String key, String... fields) {

		Jedis jedis = null;
		List<String> result = null;
		try {
			jedis = getJedis();
			result = jedis.hmget(key, fields);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：存Hash值
	 * @param key hash
	 * @return List<String>
	 * @throws JedisException
	 */
	public static String hmset(String key, Map<String,String> hash) {

		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.hmset(key, hash);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：给redis中某个key设置过期时间
	 * @param key key值
	 * @param cacheSeconds 过期时间
	 * @throws JedisException
	 */
	public static void setExpire(String key, int cacheSeconds) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (cacheSeconds >= 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：返回redis中某个list队列头长度。
	 * <p>注意:list中的元素是可以重复的</p>
	 * @param key key
	 * @throws JedisException
	 */
	public static int llen(String key) {
		Jedis jedis = null;
		int length = 0;
		try {
			jedis = getJedis();
			length = jedis.llen(key).intValue();
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return length;
	}

	/**
	 * 公共方法：给redis中某个list队列头增加一个元素。
	 * <p>注意:list中的元素是可以重复的</p>
	 * @param set 集合
	 * @param key key
	 * @param data 插入的值
	 * @throws JedisException
	 */
	public static void lpush(String key, String data) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.lpush(key, data);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：给redis中某个list队列尾增加一个元素。
	 * <p>注意:list中的元素是可以重复的</p>
	 * @param set 集合
	 * @param key key
	 * @param data 插入的值
	 * @throws JedisException
	 */
	public static void rpush(String key, String data) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.rpush(key, data);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 公共方法：给redis中某个list队列头出列一个元素。
	 * @param key key
	 * @throws JedisException
	 */
	public static String lpop(String key) {
		Jedis jedis = null;
		String str = null;
		try {
			jedis = getJedis();
			str = jedis.lpop(key);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return str;
	}

	/**
	 * 公共方法：给redis中某个list队列尾出列一个元素。
	 * @param key key
	 * @throws JedisException
	 */
	public static String rpop(String key) {
		Jedis jedis = null;
		String str = null;
		try {
			jedis = getJedis();
			str = jedis.rpop(key);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return str;
	}

	/**
	 * 公共方法：获取取redis中某个list中连续的N个元素
	 * <p>注意：该方法仅为查询，不会对list中的数据造成任何影响</p>
	 * @param key key
	 * @param srartindex 开始索引(0为第一个)
	 * @param endindex 开始索引(-1为最后一个)
	 * @throws JedisException
	 */
	public static List<String> lrange(String key, int srartindex,
			int endindex) {
		Jedis jedis = null;
		List<String> result = null;
		try {
			jedis = getJedis();
			result = jedis.lrange(key, srartindex, endindex);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：删除List中count个和value一样值的元素
	 * @param key key
	 * @param count 总数  0代表删除所有
	 * @param value 值
	 * @throws JedisException
	 */
	public static Long lrem(String key, int count, String value) {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			result = jedis.lrem(key, count, value);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：根据key删除值
	 * @param key key
	 * @return Long
	 * @throws JedisException
	 */
	public static Long del(String key) {

		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			result = jedis.del(key);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 公共方法：根据key删除值
	 * @param key key
	 * @return Long
	 * @throws JedisException
	 */
	public static Long del(byte[] key) {

		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			result = jedis.del(key);
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 获取能匹配某一种模式的key的set集合
	 * @param pattern
	 * @return
	 */
	public static Set<String> getKeys(String pattern) {

		Jedis jedis = null;
		Set<String> result = null;

		try {
			jedis = getJedis();
			result = jedis.keys(pattern);
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;

	}
	/**
	 * set集合 添加一个元素到set中 redis中set值不重复
	 * @param key
	 * @param value
	 * @return
	 */
	public static int sadd(String key,String value){
		Jedis jedis = null;
		int addcount;
		try {
			jedis = getJedis();
			addcount = jedis.sadd(key, value).intValue();
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return addcount;
	}
	/**
	 * redis中指定key的set长度
	 * @param key
	 * @param value
	 * @return
	 */
	public static int scard(String key){
		Jedis jedis = null;
		int setSize;
		try {
			jedis = getJedis();
			setSize = jedis.scard(key).intValue();
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return setSize;
	}

	/**
	 * 判断参数中指定成员是否已经存在于与Key相关联的Set集合中
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean sismember(String key, String value){
		Jedis jedis = null;
		boolean exist;
		try {
			jedis = getJedis();
			exist = jedis.sismember(key, value);
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return exist;
	}

	/**
	 * 获取所有加入的value
	 * @param key
	 * @return
	 */
	public static Set<String> smembers(String key) {
		Jedis jedis = null;
		Set<String> result = null;
		try {
			jedis = getJedis();
			result = jedis.smembers(key);
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * value自增
	 * @param key
	 * @param value
	 * @return
	 */
	public static int incr(String key){
		Jedis jedis = null;
		int result;
		try {
			jedis = getJedis();
			result = new Long(jedis.incr(key)).intValue();
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return result;
	}

	/**
	 * Redis中是否存在key
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean exists(String key){
		Jedis jedis = null;
		boolean exists;
		try {
			jedis = getJedis();
			exists = jedis.exists(key);
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return exists;
	}

	/**
	 * Redis Set的并集SUNION
	 * @param key
	 * @param value
	 * @return
	 */
	public static Set<String> sunion(String... keys){
		Jedis jedis = null;
		Set<String> unionSet;
		try {
			jedis = getJedis();
			unionSet = jedis.sunion(keys);
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return unionSet;
	}

	/**
	 * Redis Set的交集 SINTER
	 * @param key
	 * @param value
	 * @return
	 */
	public static Set<String> sinter(String... keys){
		Jedis jedis = null;
		Set<String> interSet;
		try {
			jedis = getJedis();
			interSet = jedis.sinter(keys);
		} catch(JedisException e) {
			throw new JedisException(e);
		} finally {
			closeJedis(jedis);
		}
		return interSet;
	}

    public static String mk_sets_key(String type, String key)
    {
        return type + ":" + key.toLowerCase();
    }

    public static String mk_score_key(String type, String id)
    {
        return type + ":_score_:" + id;
    }

    public static String mk_condition_key(String type, String field, String id)
    {
        return type + ":_by:_" + field + ":" + id;
    }

    public static String mk_complete_key(String type)
    {
        return "Compl" + type;
    }

    /**
     *
     * @param jedis
     * @param type
     * @param ids
     * @param options
     * @return
     */
    public static List<Object> mget(Jedis jedis, String type, List<String> ids, LinkedHashMap options)
    {
        List<Object> result = new ArrayList<Object>();
        //String sort_field = (String) (options.get("sort_field") == null ? "id" : options.get("sort_field"));
        if (null == ids || ids.isEmpty())
        {
            return result;
        }
        try
        {
            for (int j = 0; j < ids.size(); j++)
            {
            	result.add(SerializationUtils.deserialize(jedis.get(ids.get(j).getBytes())));
            }

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
