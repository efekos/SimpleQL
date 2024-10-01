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

/**
 * An enum used to change behavior of a {@link Database} or {@link Table} based on its connection URL.
 *
 * @since 1.0
 */
public enum DatabaseType {

    /**
     * MySQL databases.
     */
    MYSQL(true, "mysql"),

    /**
     * SQLite databases.
     */
    SQLITE(false, "sqlite");

    private final boolean createSchema;
    private final String name;

    DatabaseType(boolean createSchema, String name) {
        this.createSchema = createSchema;
        this.name = name;
    }

    /**
     * Returns whether this SQL database has a sub-database system where a schema must be created and tables should be
     * created under it.
     *
     * @return Whether this database should have schemas or not.
     */
    public boolean shouldCreateSchema() {
        return createSchema;
    }

    /**
     * Returns a unique, short name to identify this DatabaseType and distinguish it from others. This name is usually
     * the first word after {@code jdbc} in the database url. This value is also used by
     * {@link DatabaseInformation#getType()} to figure out the database type that suits the information.
     *
     * @return Name of this database type.
     */
    public String getName() {
        return name;
    }
}
