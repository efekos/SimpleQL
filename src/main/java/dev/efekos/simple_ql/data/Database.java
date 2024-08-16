package dev.efekos.simple_ql.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Database {

    private Connection connection;
    private final DatabaseInformation information;
    private final Map<String,Table<?>> tables = new HashMap<>();

    public Database(DatabaseInformation information) {
        this.information = information;
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(information.getConnectionUrl(), information.getUsername(), information.getPassword());
        if(information.getType().shouldCreateSchema()){
            connection.prepareStatement("CREATE SCHEMA "+information.getDatabaseName()+";").executeUpdate();
            connection.prepareStatement("USE "+information.getDatabaseName()+";").executeUpdate();
        }
    }

    public <T extends TableRow<T>> Table<T> registerTable(String name,Class<T> clazz){
        if(tables.containsKey(name))throw new IllegalStateException("A table with name '"+name+"' is already registered.");
        Table<T> table = new Table<>(this, name, clazz);
        table.checkExistent();
        tables.put(name, table);
        return table;
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