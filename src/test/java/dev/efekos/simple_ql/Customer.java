package dev.efekos.simple_ql;

import dev.efekos.simple_ql.annotation.Primary;
import dev.efekos.simple_ql.data.Table;
import dev.efekos.simple_ql.data.TableRow;

import java.util.UUID;

public class Customer extends TableRow<Customer> {

    @Primary
    private UUID id;

    private String name;

    public Customer(Class<Customer> clazz, Table<Customer> parentTable) {
        super(clazz,parentTable);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
