package io.ituknown.mysql;

import io.ituknown.mysql.cli.SQLClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class MySQLVertx extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        SQLClient.init(vertx, e -> {
            e.printStackTrace();
            vertx.close();
        });

        SQLClient.withConnection(conn -> conn.preparedQuery("SELECT * FROM buyer").execute())
                .onSuccess(this::handleRows);

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        SQLClient.release();
    }

    public void handleRows(RowSet<Row> event) {
        for (Row row : event) {
            System.out.println(row.getLong("id"));
            System.out.println(row.getString("name"));
            System.out.println(row.getString("mobile"));
        }
    }
}