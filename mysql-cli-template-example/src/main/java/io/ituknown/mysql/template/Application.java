package io.ituknown.mysql.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ituknown.mysql.template.jackson.JacksonConfig;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {

        // 配置 vertx 的 Jackson 消息转换器
        // vertx 的默认 Jackson 配置特别简单, 无法处理复杂的数据. 如 LocalDateTime
        setDefaultJackson(DatabindCodec.mapper());
        setDefaultJackson(DatabindCodec.prettyMapper());

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MySQLTemplateVertx());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            vertx.close(event -> countDownLatch.countDown());
            try {
                countDownLatch.await(3L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        // Test Shutdown
        try {
            TimeUnit.SECONDS.sleep(3L);
            System.out.println("主动调用 exit");
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setDefaultJackson(final ObjectMapper mapper) {

        // 序列化所有字段
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 忽略未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 设置时区
        JacksonConfig.configureTimeZone(mapper);

        // NULL 值处理
        JacksonConfig.configureNullObject(mapper);

        // java.util.Date 日期格式 处理
        JacksonConfig.configureDate(mapper);

        // Java8 日期处理格式
        JacksonConfig.configureObjectMapper4Jsr310(mapper);

        // BigDecimal 自定义序列化
        // JacksonConfig.registerModule(mapper, BigDecimal.class, new BigDecimalAsStringJsonSerializer());
    }
}
