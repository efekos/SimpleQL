package dev.efekos.simple_ql.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SetterAction<T> {
    void set(PreparedStatement stmt, int index, T value) throws SQLException;
}