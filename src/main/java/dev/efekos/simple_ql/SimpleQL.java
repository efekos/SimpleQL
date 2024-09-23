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

package dev.efekos.simple_ql;

import dev.efekos.simple_ql.data.Database;
import dev.efekos.simple_ql.data.DatabaseInformation;

import java.sql.SQLException;

/**
 * Main class of SimpleQL, used to create {@link Database}s.
 *
 * @since 1.0
 */
public final class SimpleQL {

    /**
     * Creates a new {@link Database} based on given {@link DatabaseInformation}, and starts a connection.
     *
     * @param information Information about the database to connect.
     * @return A {@link Database} instance that is already connected which means running {@link Database#connect()}.
     */
    public static Database createDatabase(DatabaseInformation information) {
        Database database = new Database(information);
        try {
            database.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return database;
    }

    /**
     * Creates a new {@link Database} based on given url, username and password.
     *
     * @param url      Database URL. See {@link java.sql.DriverManager#getConnection(String)} for more information.
     * @param user     Username of the account being used to connect to the database.
     * @param password Password of the account being used to connect to the database.
     * @return A {@link Database} instance that is already connected which means running {@link Database#connect()}.
     */
    public static Database createDatabase(String url, String user, String password) {
        return createDatabase(new DatabaseInformation(url, user, password));
    }

    /**
     * Creates a new {@link Database} based on given url, database name, username and password.
     *
     * @param url          Database URL. See {@link java.sql.DriverManager#getConnection(String)} for more information.
     * @param databaseName Name of the database that this {@link Database} instance will be connected to.
     * @param user         Username of the account being us ed to connect to this database.
     * @param password     Password of the account being used to connect to this database.
     * @return A {@link Database} instance that is already connected which means running {@link Database#connect()}.
     */
    public static Database createDatabase(String url, String databaseName, String user, String password) {
        return createDatabase(new DatabaseInformation(url, user, password, databaseName));
    }

    /**
     * Creates a new {@link Database} based on given url. The database must require no accounts at all as this method
     * doesn't consume any credentials.
     *
     * @param url Database URL. See {@link java.sql.DriverManager#getConnection(String)} for more information.
     * @return A {@link Database} instance that is already connected which means running {@link Database#connect()}.
     */
    public static Database createDatabase(String url) {
        return createDatabase(new DatabaseInformation(url, null, null));
    }

    /**
     * Creates a new {@link Database} based on given url and database name. The database must require no accounts at all
     * as this method doesn't consume any credentials.
     *
     * @param url          Database URL. See {@link java.sql.DriverManager#getConnection(String)} for more information.
     * @param databaseName Name of the database that this {@link Database} instance will be connected to.
     * @return A {@link Database} instance that is already connected which means running {@link Database#connect()}.
     */
    public static Database createDatabase(String url, String databaseName) {
        return createDatabase(new DatabaseInformation(url, null, null, databaseName));
    }

}