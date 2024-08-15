package dev.efekos.simple_ql.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Database {

    private Connection connection;
    private final DatabaseInformation information;

    public Database(DatabaseInformation information) {
        this.information = information;
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(information.getConnectionUrl(), information.getUsername(), information.getPassword());
    }

    public Connection getConnection() {
        return connection;
    }

    public Optional<SQLException> disconnect() {
        try {
            connection.close();
            return Optional.empty();
        } catch (SQLException e){
            return Optional.of(e);
        }
    }

}