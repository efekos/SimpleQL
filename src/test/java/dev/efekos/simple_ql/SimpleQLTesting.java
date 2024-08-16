package dev.efekos.simple_ql;

import dev.efekos.simple_ql.data.Database;
import dev.efekos.simple_ql.data.Table;

import java.util.Optional;
import java.util.UUID;

public class SimpleQLTesting {

    public static void main(String[] args) throws Exception {
        // Database connection. Database name is ignored since this is SQLite.
        Database database = SimpleQL.createDatabase("jdbc:sqlite:run/test.sqlite","simpleql");
        database.connect();

        // Getting tables.
        Table<Customer> customers = database.registerTable("customers", Customer.class);

        // Data insertion.
        UUID id = UUID.randomUUID();
        Customer customer = customers.insertRow(c -> {
            c.setId(id);
            c.setName("John Doe");
            c.setMoney(new CustomerMoney(50,0));
            c.setGender(CustomerGender.FEMALE);
        });

        Optional<Customer> row = customers.getRow(id);
        row.ifPresent(System.out::println);

        // Data updating.
        customer.setName("John Boe");
        CustomerMoney money = customer.getMoney();
        money.setCents(10);
        customer.setMoney(money);
        customer.setGender(CustomerGender.MALE);
        customer.clean();

        // Data deleting.
        customer.delete();

        // Database disconnection.
        database.disconnect();
    }

}
