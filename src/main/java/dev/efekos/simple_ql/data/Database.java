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

/**
 * Main class of SimpleQL, used to manage a database and create tables inside it.
 *
 * @since 1.0
 */
public class Database {

    private final DatabaseInformation information;
    private final Map<String, Table<?>> tables = new HashMap<>();
    private Connection connection;

    /**
     * Creates a new database without connecting to it.
     *
     * @param information Necessary information about the database such as its URL and login credentials.
     */
    public Database(DatabaseInformation information) {
        this.information = information;
    }

    /**
     * Opens a new connection and sets it to current connection, which is accessible through {@link #getConnection()}.
     *
     * @throws SQLException If a new connection could not be established for several reasons, such as invalid credentials,
     *                      database not being on or not being able to create/use the database name.
     */
    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(information.getConnectionUrl(), information.getUsername(), information.getPassword());
        if (information.getType().shouldCreateSchema()) {
            connection.prepareStatement("CREATE SCHEMA " + information.getDatabaseName() + ";").executeUpdate();
            connection.prepareStatement("USE " + information.getDatabaseName() + ";").executeUpdate();
        }
    }

    /**
     * Table getter based on table name. Table must be registered first using {@link #registerTable(String, Class, Implementor[])}
     * in order to appear here.
     *
     * @param tableName Name of the table.
     * @return A nullable {@link Optional} that will contain a {@link Table} if it was registered before.
     */
    public Optional<Table<?>> getTable(String tableName) {
        return Optional.ofNullable(tables.get(tableName));
    }

    /**
     * Registers a new table, creates it on the database and returns it.
     *
     * @param name         Name of the table.
     * @param clazz        Class which rows of this table will be in.
     * @param implementors List of {@link Implementor}s to handle custom classes used in given {@link TableRow} type.
     * @param <T>          Type which rows of this table will be in.
     * @return A {@link Table} instance that represents the database table that has the same name.
     * @throws IllegalStateException if a table with the same name was already registered before. see {@link #getTable(String)}
     *                               if you have to access a table after creating it.
     */
    public <T extends TableRow<T>> Table<T> registerTable(String name, Class<T> clazz, Implementor<?, ?>... implementors) {
        if (tables.containsKey(name))
            throw new IllegalStateException("A table with name '" + name + "' is already registered.");
        Table<T> table = new Table<>(this, name, clazz, implementors);
        table.checkExistent();
        tables.put(name, table);
        return table;
    }

    /**
     * Returns the currently open connection if there is one.
     *
     * @return A {@link Connection} instance if there is one open, {@code null} otherwise.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Tries to disconnect from the currently open connection, ignoring all {@link SQLException}s as there this method
     * will be run moments before rest of the application stops in most cases.
     *
     * @return A nullable {@link Optional} of an {@link SQLException} in case it can be handled.
     */
    public Optional<SQLException> disconnect() {
        if (connection == null) return Optional.empty();
        try {
            connection.close();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(e);
        }
    }

}