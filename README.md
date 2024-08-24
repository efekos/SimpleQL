# SimpleQL

This library lets you use an SQL database without knowing the SQL language at all! You can easily create tables, insert,
update and delete rows and manage database connections.

Here is a working example:

```java
package dev.efekos.simple_ql;

import dev.efekos.simple_ql.data.Database;
import dev.efekos.simple_ql.data.Table;

import java.util.Optional;
import java.util.UUID;

// Checkout test classes to learn more
public class SimpleQLExample {

    public static void main(String[] args) throws Exception {
        // Database connection. Database name is ignored since this is SQLite.
        Database database = SimpleQL.createDatabase("jdbc:sqlite:my.db","simpleql");
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

        // Getting data.
        Optional<Customer> row = customers.getRow(id);

        // Data updating.
        customer.setName("John Boe");
        CustomerMoney money = customer.getMoney();
        money.setCents(10);
        customer.setMoney(money);
        customer.setGender(CustomerGender.MALE);
        customer.clean(); // saves changes to database

        // Deleting data.
        customer.delete();

        // Database disconnection.
        database.disconnect();
    }

}

```

## License

This project is licensed under the MIT License.