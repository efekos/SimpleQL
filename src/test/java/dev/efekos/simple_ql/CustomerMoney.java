package dev.efekos.simple_ql;

import dev.efekos.simple_ql.data.TableRowTypeAdapter;

import java.util.Objects;

public class CustomerMoney implements TableRowTypeAdapter {

    private int dollars;
    private int cents;

    public CustomerMoney(int dollars, int cents) {
        this.dollars = dollars;
        this.cents = cents;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    public int getCents() {
        return cents;
    }

    public void setCents(int cents) {
        this.cents = cents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerMoney that = (CustomerMoney) o;
        return dollars == that.dollars && cents == that.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dollars, cents);
    }

    @Override
    public String toString() {
        return "CustomerMoney{" +
                "dollars=" + dollars +
                ", cents=" + cents +
                '}';
    }

    @Override
    public String adapt() {
        return dollars + "." + cents;
    }

    public static CustomerMoney readAdapted(String input) {
        String[] parts = input.split("\\.");
        int dollars = Integer.parseInt(parts[0]);
        int cents = Integer.parseInt(parts[1]);
        return new CustomerMoney(dollars, cents);
    }
}
