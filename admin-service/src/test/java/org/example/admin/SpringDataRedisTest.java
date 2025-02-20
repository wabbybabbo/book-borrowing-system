package org.example.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//如果测试失败，可以尝试先执行一下maven生命周期中的clean
@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        HashOperations<String, String, Object> hashoperations = redisTemplate.opsForHash();
        ListOperations<String, Object> listoperations = redisTemplate.opsForList();
        SetOperations<String, Object> setoperations = redisTemplate.opsForSet();
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        //设置指定key的值，并将key的过期时间设为60秒
        // SETEX key seconds value
        valueOperations.set("code", "yzm", 60, TimeUnit.SECONDS);

        //获取指定key的值
        //GET key
        String code = (String) valueOperations.get("code");
        System.out.println("code: " + code);

        //只有在key不存在时设置key的值
        //SETNX key value
        valueOperations.setIfAbsent("code", "yangzhengma");
    }

    /**
     * 操作哈希类型的数据
     */
    @Test
    public void testHash() {
        HashOperations<String, String, String> hashoperations = redisTemplate.opsForHash();

        //将哈希表key中的字段field的值设为value
        //HSET key field value
        hashoperations.put("myHash", "field1", "value1");
        hashoperations.put("myHash", "field2", "value2");

        //获取存储在哈希表中指定字段的值
        //HGET key field
        String value1 = hashoperations.get("myHash", "field1");
        System.out.println("value1: " + value1);

        //获取哈希表中所有字段
        //HKEYS key
        Set<String> fields = hashoperations.keys("myHash");
        System.out.println("fields: " + fields);

        //获取哈希表中所有值
        //HVALS key
        List<String> values = hashoperations.values("myHash");
        System.out.println("values: " + values);

        //删除存储在哈希表中的指定字段
        //HDEL key field
        hashoperations.delete("hkey", "field");
    }

    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();

        //将一个或多个值插入到列表头部
        //LPUSH key value1 [value2]
        listOperations.leftPushAll("myList", new String[]{"a"}, new String[]{"b"}, new String[]{"c"});
        listOperations.leftPush("myList", "d");

        //获取列表指定范围内的元素
        //LRANGE key start stop
        List<Object> myList = listOperations.range("myList", 0, -1);
        System.out.println("myList: " + myList);

        //移除并获取列表最后一个元素
        //RPOP key
        String lastValue = (String) listOperations.rightPop("myList");
        System.out.println("lastValue: " + lastValue);

        //获取列表长度
        //LLEN key
        Long size = listOperations.size("myList");
        System.out.println("size: " + size);
    }

    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet() {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();

        //向集合添加一个或多个成员，并返回添加的成员个数
        //SADD key member1 [member2]
        setOperations.add("set1", new String[]{"a"}, new String[]{"b"}, new String[]{"c"}, new String[]{"d"});
        setOperations.add("set2", new String[]{"a"}, new String[]{"b"}, new String[]{"x"}, new String[]{"y"});

        //获取集合中的所有成员
        //SMEMBERS key
        Set<Object> members = setOperations.members("set1");
        System.out.println("members: " + members);

        //获取集合的成员数量
        //SCARD key
        Long size = setOperations.size("set1");
        System.out.println("size: " + size);

        //获取给定所有集合的交集
        //SINTER key1 [key2]
        Set<Object> intersect = setOperations.intersect("set1", "set2");
        System.out.println("intersect: " + intersect);

        //获取所有给定集合的并集
        //SUNION key1 [key2]
        Set<Object> union = setOperations.union("set1", "set2");
        System.out.println("union: " + union);

        //删除集合中一个或多个成员
        //SREM key member1 [member2]
        setOperations.remove("set1", "a", "b", "c", "d");
        setOperations.remove("set2", "a", "b", "x", "y");
    }

    /**
     * 操作有序集合类型的数据
     */
    @Test
    public void testZset() {
        ZSetOperations<String, Object> zsetOperations = redisTemplate.opsForZSet();

        //向有序集合添加一个或多个成员，并返回添加的成员数量
        //ZADD key score1 member1 [score2 member2]
        zsetOperations.add("zset", "a", 1);
        zsetOperations.add("zset", "b", 2);
        zsetOperations.add("zset", "c", 3);

        //有序集合中对指定成员的分数加上增量increment
        //ZINCRBY key increment member
        zsetOperations.incrementScore("zset", "a", 10);

        //通过索引区间返回有序集合中指定区间内的成员
        //ZRANGE key start stop [WITHSCORES]
        Set<Object> zset = zsetOperations.range("zset", 0, -1);
        System.out.println("zset: " + zset);

        //移除有序集合中的一个或多个成员
        //ZREM key member1 [member2]
        zsetOperations.remove("zset", "a", "b", "c");
    }

    /**
     * 通用命令操作
     */
    @Test
    public void testCommon() {
        redisTemplate.opsForValue().set("str", "abc", 10, TimeUnit.SECONDS);

        //查找所有符合给定模式(pattern)的key
        //KEYS pattern
        Set<String> keys = redisTemplate.keys("*");
        System.out.println("keys: " + keys);

        //检查给定key是否存在
        //EXISTS key
        Boolean hasKey = redisTemplate.hasKey("str");
        System.out.println("hasKey: " + hasKey);

        //返回key所储存的值的类型
        //TYPE key
        DataType type = redisTemplate.type("str");
        if (type != null) {
            System.out.println("type: " + type.name());
        }

        //当key存在时删除key
        //DEL key
        redisTemplate.delete("str");
    }

}