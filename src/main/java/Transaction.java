import GenericCRUD.SQLOperations;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {

    private Connection connection;

    public Transaction(Connection connection) {
        this.connection = connection;
    }

    public void start(SQLOperations operations) {
        try {
            connection.setAutoCommit(false);
            operations.execute(connection);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
