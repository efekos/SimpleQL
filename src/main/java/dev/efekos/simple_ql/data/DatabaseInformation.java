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

import java.util.Objects;

/**
 * A data class used to store information of a database.
 *
 * @since 1.0
 */
public class DatabaseInformation {

    private String connectionUrl;
    private String username;
    private String password;
    private String databaseName = "simple_ql";

    /**
     * Creates a new DatabaseInformation.
     *
     * @param connectionUrl URL of the database. See {@link java.sql.DriverManager#getConnection(String)} for more information.
     * @param username      Username.
     * @param password      Password.
     * @param databaseName  Name of the database to connect. Not necessary as there is a default one, and will be ignored
     *                      if detected database type doesn't support sub-databases (such as SQLite).
     */
    public DatabaseInformation(String connectionUrl, String username, String password, String databaseName) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    /**
     * Creates a new DatabaseInformation
     *
     * @param connectionUrl URL of the database. See {@link java.sql.DriverManager#getConnection(String)} for more information.
     * @param username      Username.
     * @param password      Password.
     */
    public DatabaseInformation(String connectionUrl, String username, String password) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DatabaseInformation{" +
                "connectionUrl='" + connectionUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }

    /**
     * Finds a database type suitable for this set of DatabaseInformation based on the URL.
     *
     * @return A suitable {@link DatabaseType} if found, {@code null} otherwise.
     */
    public DatabaseType getType() {
        for (DatabaseType type : DatabaseType.values())
            if (connectionUrl.startsWith("jdbc:" + type.getName())) return type;
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseInformation that = (DatabaseInformation) o;
        return Objects.equals(getConnectionUrl(), that.getConnectionUrl()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(databaseName, that.databaseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getConnectionUrl(), getUsername(), getPassword(), databaseName);
    }

    /**
     * Returns the database URL.
     *
     * @return Database URL.
     */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
     * Returns the username if present.
     *
     * @return Username if present, {@code null} otherwise.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password if present.
     *
     * @return Password if present, {@code null} otherwise.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the database  name if present.
     *
     * @return Database name if present, {@code simple_ql} otherwise.
     */
    public String getDatabaseName() {
        return databaseName;
    }

}
