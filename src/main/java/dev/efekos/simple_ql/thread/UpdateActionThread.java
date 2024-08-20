package dev.efekos.simple_ql.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateActionThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(UpdateActionThread.class);
    private final Connection connection;
    private final String statement;
    private final StatementPreparer consumer;

    public UpdateActionThread(Connection connection, String statement, StatementPreparer consumer) {
        super("SimpleQL-UpdateThread");
        this.connection = connection;
        this.statement = statement;
        this.consumer = consumer;
    }

    @Override
    public synchronized void start() {
        try (PreparedStatement stmt = connection.prepareStatement(statement)) {
            PreparedStatement applied = consumer.prepare(stmt);
            applied.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not update database.", e);
        } catch (Exception e) {
            log.error("Statement preparer error.", e);
        }
    }

    @FunctionalInterface
    public interface StatementPreparer {
        PreparedStatement prepare(PreparedStatement stmt) throws Exception;
    }
}
