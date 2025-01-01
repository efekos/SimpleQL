/*
 * MIT License
 *
 * Copyright (c) 2025 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.efekos.simple_ql.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A thread used to execute database actions where the result is ignored.
 *
 * @since 1.0
 */
public class UpdateActionThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(UpdateActionThread.class);
    private final Connection connection;
    private final String statement;
    private final StatementPreparer consumer;

    /**
     * Creates a new thread
     *
     * @param connection Connection to execute statements on.
     * @param statement  Statement to execute.
     * @param consumer   A preparer to prepare a statement by setting values properly.
     */
    public UpdateActionThread(Connection connection, String statement, StatementPreparer consumer) {
        super("SimpleQL-UpdateThread");
        this.connection = connection;
        this.statement = statement;
        this.consumer = consumer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try (PreparedStatement stmt = connection.prepareStatement(statement)) {
            PreparedStatement applied = consumer.prepare(stmt);
            applied.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not update database.", e);
        } catch (Exception e) {
            log.error("Statement preparer error.", e);
        }
    }

    /**
     * A functional interface that is used to prepare a statement by setting values properly before execution.
     */
    @FunctionalInterface
    public interface StatementPreparer {

        /**
         * Prepares the given statement and returns it.
         *
         * @param stmt A statement that already has a query added on it.
         * @return Same statement with all arguments needed.
         * @throws Exception Just to allow users to throw exceptions.
         */
        PreparedStatement prepare(PreparedStatement stmt) throws Exception;

    }

}
