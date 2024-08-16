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

    public static Database createDatabase(String url, String user, String password){
        return createDatabase(new DatabaseInformation(url, user, password));
    }


    public static Database createDatabase(String url, String databaseName, String user, String password){
        return createDatabase(new DatabaseInformation(url, user, password,databaseName));
    }

    public static Database createDatabase(String url){
        return createDatabase(new DatabaseInformation(url,null,null));
    }


    public static Database createDatabase(String url,String databaseName){
        return createDatabase(new DatabaseInformation(url,null,null, databaseName));
    }

}