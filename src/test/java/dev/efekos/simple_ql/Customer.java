package dev.efekos.simple_ql;

import dev.efekos.simple_ql.annotation.Primary;
import dev.efekos.simple_ql.annotation.Type;
import dev.efekos.simple_ql.data.Table;
import dev.efekos.simple_ql.data.TableRow;

import java.util.UUID;

public class Customer extends TableRow<Customer> {

    @Primary
    private UUID id;

    private String name;

    private CustomerMoney money;

    public Customer(Class<Customer> clazz, Table<Customer> parentTable) {
        super(clazz,parentTable);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
        super.markDirty("id");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        super.markDirty("name");
    }

    public CustomerMoney getMoney() {
        return money;
    }

    public void setMoney(CustomerMoney money) {
        this.money = money;
        super.markDirty("money");
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
