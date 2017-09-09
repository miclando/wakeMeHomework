package my.homework;
import io.vertx.core.Vertx;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;


public class Starter {


    public static void main(String [] args){


        Vertx vertx = Vertx.vertx();
        vertx.createHttpClient();



//        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
//        try (Jedis jedis = pool.getResource()) {
//
//
//            jedis.set("foo", "bar");
//            String foobar = jedis.get("foo");
//
//            jedis.zadd("sose", 0, "car");
//            jedis.zadd("sose", 0, "bike");
//            Set<String> sose = jedis.zrange("sose", 0, -1);
//        }
///// ... when closing your application:
//        pool.destroy();
//        Vertx vertx = Vertx.vertx();

        try {
            new VertxHttpServerVerticle().start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
