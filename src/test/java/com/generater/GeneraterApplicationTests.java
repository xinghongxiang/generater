package com.generater;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GeneraterApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	// 缓存的KEY
	public static final String CACHE_KEY = "HELLO";

	@Test
	void contextLoads() {
		// 1.过期时间今晚12点
		long time = new Date().getTime();
		Calendar calendar =  Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		long surplusSecond = (calendar.getTime().getTime() - time) / 1000;

		// 模拟生成1000个
		for (int i = 0; i < 100; i++) {
			// 2.主要依赖于Redis单线程来生成主键，不会重复
			RedisAtomicLong counter = new RedisAtomicLong(CACHE_KEY, redisTemplate.getConnectionFactory());
			if((null == counter || counter.longValue() == 1) ){
				// 每天晚上12点后在从1开始生成
				counter.expire(surplusSecond, TimeUnit.SECONDS);
				//counter.expireAt(new Date()); // 测试当前时间过期，是否重新生成
			}
			Long id = counter.incrementAndGet();
			System.out.println("生成id为：" + getSequence(id));
		}

	}

	/**
	 * 最大位数DEFAULT_LENGTH,不足前面补零，超过返回原值
	 */
	static final int DEFAULT_LENGTH = 4;
	public static String getSequence(long seq) {
		String str = String.valueOf(seq);
		int len = str.length();
		if (len >= DEFAULT_LENGTH) {
			return str;
		}
		int rest = DEFAULT_LENGTH - len;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rest; i++) {
			sb.append('0');
		}
		sb.append(str);
		return sb.toString();
	}

}
