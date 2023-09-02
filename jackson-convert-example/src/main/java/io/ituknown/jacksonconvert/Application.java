package io.ituknown.jacksonconvert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ituknown.jacksonconvert.jackson.BigDecimalAsStringJsonSerializer;
import io.ituknown.jacksonconvert.jackson.JacksonConfig;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;

import java.math.BigDecimal;

public class Application {
    public static void main(String[] args) {
        // 配置 vertx 的 Jackson 消息转换器
        // vertx 的默认 Jackson 配置特别简单, 无法处理复杂的数据. 如 LocalDateTime
        setDefaultJackson(DatabindCodec.mapper());
        setDefaultJackson(DatabindCodec.prettyMapper());

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ExampleHttpServerVertx());
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
        JacksonConfig.registerModule(mapper, BigDecimal.class, new BigDecimalAsStringJsonSerializer());
    }
}