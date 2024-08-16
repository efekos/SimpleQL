package dev.efekos.simple_ql.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface GetterAction<T> {
    T get(ResultSet s,String columnName) throws SQLException;
}