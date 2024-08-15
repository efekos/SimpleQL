package dev.efekos.simple_ql;

import dev.efekos.simple_ql.data.Database;
import dev.efekos.simple_ql.data.DatabaseInformation;

import java.sql.SQLException;

public final class SimpleQL {

    public static Database createDatabase(DatabaseInformation information){
        Database database = new Database(information);
        try {
            database.connect();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return database;
    }

}