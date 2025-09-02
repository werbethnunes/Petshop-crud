package br.fiap.petshop.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    private ConnectionFactory() {}

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:file:./data/petshop-db;MODE=MySQL;AUTO_SERVER=TRUE",
                "sa", ""
        );
    }
}