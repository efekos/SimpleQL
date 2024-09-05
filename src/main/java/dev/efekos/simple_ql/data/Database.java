/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package dev.efekos.simple_ql.data;

import dev.efekos.simple_ql.implementor.Implementor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Database {

    private final DatabaseInformation information;
    private final Map<String, Table<?>> tables = new HashMap<>();
    private Connection connection;

    public Database(DatabaseInformation information) {
        this.information = information;
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(information.getConnectionUrl(), information.getUsername(), information.getPassword());
        if (information.getType().shouldCreateSchema()) {
            connection.prepareStatement("CREATE SCHEMA " + information.getDatabaseName() + ";").executeUpdate();
            connection.prepareStatement("USE " + information.getDatabaseName() + ";").executeUpdate();
        }
    }

    public Optional<Table<?>> getTable(String tableName) {
        return Optional.ofNullable(tables.get(tableName));
    }

    public <T extends TableRow<T>> Table<T> registerTable(String name, Class<T> clazz, Implementor<?,?>... implementors) {
        if (tables.containsKey(name))
            throw new IllegalStateException("A table with name '" + name + "' is already registered.");
        Table<T> table = new Table<>(this, name, clazz,implementors);
        table.checkExistent();
        tables.put(name, table);
        return table;
    }

    public Connection getConnection() {
        return connection;
    }

    public Optional<SQLException> disconnect() {
        try {
            connection.close();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(e);
        }
    }

}