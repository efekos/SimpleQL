package dev.efekos.simple_ql;

import dev.efekos.simple_ql.data.Database;

public class SimpleQLTesting {

    public static void main(String[] args) throws Exception {
        Database database = SimpleQL.createDatabase("jdbc:sqlite:test.db");
        database.connect();
        database.registerTable("customers",Customer.class);
    }

}
