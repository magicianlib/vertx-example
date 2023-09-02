package io.ituknown.mysql.template;

import io.ituknown.mysql.template.cli.MySQLClient;
import io.ituknown.mysql.template.model.SysAdministrativeRegion;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;

public class MySQLTemplateVertx extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        MySQLClient.init(vertx, e -> vertx.close());

        // to class
        RowSet<SysAdministrativeRegion> result1 = MySQLClient.forTemplateQuery("select add_time, update_time from sys_administrative_region where id = #{id}", SysAdministrativeRegion.class)
                .execute(Collections.singletonMap("id", "06a79cd3eed811e885da5615b7fd4e65"))
                .onFailure(Throwable::printStackTrace)
                .onSuccess(rows -> {
                    for (SysAdministrativeRegion row : rows) {
//                        System.out.println(row.getAddTime() + " --------- " + row.getUpdateTime());
                    }
                }).result();

        // to json
        RowSet<JsonObject> result2 = MySQLClient.forTemplateQuery("select add_time, update_time from sys_administrative_region where id = #{id}", Row::toJson)
                .execute(Collections.singletonMap("id", "06a79cd3eed811e885da5615b7fd4e65"))
                .onFailure(Throwable::printStackTrace)
                .onSuccess(rows -> {
                    for (JsonObject row : rows) {
                        System.out.println(row.getString("add_time"));
                        TemporalAccessor addTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").parse(row.getString("add_time"));
                    }
                }).result();

        // close
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        MySQLClient.release();
    }
}