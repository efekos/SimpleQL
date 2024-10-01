# SimpleQL

![](https://flat.badgen.net/github/license/efekos/SimpleQL)
![](https://flat.badgen.net/github/release/efekos/SimpleQL)
![](https://flat.badgen.net/github/releases/efekos/SimpleQL)
![](https://flat.badgen.net/github/stars/efekos/SimpleQL)
![](https://flat.badgen.net/github/issues/efekos/SimpleQL)
![](https://flat.badgen.net/github/prs/efekos/SimpleQL)
[![](https://flat.badgen.net/static/JavaDoc/Available/green)](https://efekos.dev/javadoc/simpleql/1.0/index.html)

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

# Installation

## Maven

1. Add this repository if you don't have it in your repositories.
````xml
<repository>
    <id>efekosdev</id>
    <url>https://efekos.dev/maven</url>
</repository>
````

2. Add this dependency. Replace the version with the latest version, which is ![](https://badgen.net/github/release/efekos/SimpleQL).
````xml
<dependency>
    <groupId>dev.efekos</groupId>
    <artifactId>SimpleQL</artifactId>
    <version>1.0.0</version>
</dependency> 
````

## Gradle

1. Add this repository if you don't have it in your repositories
````gradle
maven { url 'https://efekos.dev/maven' } 
````

2. Add this dependency. Replace the version with the latest version. which is ![](https://badgen.net/github/release/efekos/SimpleQL).
````gradle
implementation 'dev.efekos:SimpleQL:1.0.0' 
````

# License

This project is licensed under the MIT License.