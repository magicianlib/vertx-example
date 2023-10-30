package io.ituknown.mysql.template;

import io.ituknown.mysql.template.cli.SQLClient;
import io.ituknown.mysql.template.model.SysAdministrativeRegion;
import io.ituknown.mysql.template.model.SysAdministrativeRegionExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.Collections;

public class MySQLTemplateVertx extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        SQLClient.init(vertx, e -> vertx.close());

        // to class
        RowSet<SysAdministrativeRegion> result1 = SQLClient.forTemplateQuery("select add_time, update_time from sys_administrative_region where id = #{id}", SysAdministrativeRegion.class)
                .execute(Collections.singletonMap("id", "06a79cd3eed811e885da5615b7fd4e65"))
                .onFailure(Throwable::printStackTrace)
                .onSuccess(rows -> {
                    for (SysAdministrativeRegion row : rows) {
                        System.out.println(row.getAddTime() + " --------- " + row.getUpdateTime());
                    }
                }).result();

        // to json
        RowSet<JsonObject> result2 = SQLClient.forTemplateQuery("select add_time, update_time from sys_administrative_region where id = #{id}", Row::toJson)
                .execute(Collections.singletonMap("id", "06a79cd3eed811e885da5615b7fd4e65"))
                .onFailure(Throwable::printStackTrace)
                .onSuccess(rows -> {
                    for (JsonObject row : rows) {
                        System.out.println(row.getString("add_time"));
                        TemporalAccessor addTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").parse(row.getString("add_time"));
                    }
                }).result();

        // 事物操作
        Future<Boolean> result = SQLClient.withTransaction(conn -> SQLClient.forTemplateUpdate(conn, "INSERT INTO role(role, reversion) VALUES(\"admin\", 0)")
                .execute(Collections.emptyMap())
                .compose(r -> {
                    if (r.rowCount() == 0) {
                        return Future.failedFuture("事物失败回滚");
                    }
                    // 角色自增ID
                    Long autoIncrementId = r.property(MySQLClient.LAST_INSERTED_ID);
                    return Future.succeededFuture(autoIncrementId);
                }).compose(id -> SQLClient.forTemplateUpdate(conn, "INSERT INTO user(role_id, username, age) VALUES(#{roleId}, \"bob\", 18)")
                        .execute(Collections.singletonMap("roleId", id))
                        .compose(r -> {
                            if (r.rowCount() == 0) {
                                return Future.failedFuture("事物失败回滚");
                            }
                            return Future.succeededFuture(true);
                        })
                ).onFailure(e -> {
                    // 事物失败异常处理
                }).onSuccess(r -> {
                    // 事物成功处理
                }).onComplete(r -> {
                    // 执行完成处理(事物成功或失败都会执行)
                })
        );

        if (Boolean.TRUE.equals(result.result())) {
            System.out.println("用户角色保存成功");
        }

        // close
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        SQLClient.release();
    }
}