package database;

import dto.TransactionDTO;
import java.sql.*;
import java.util.ArrayList;

public class TransactionDAO {
    private String url = "jdbc:postgresql://localhost:5432/blockchain_transactions";
    private String username = "postgres";
    private String password = "12345";

    public void saveTransactions(ArrayList<TransactionDTO> transactions){
        try {
            // Open connection
            Connection conn = DriverManager.getConnection(url, username, password);

            // Prepare SQL statement
            String sql = "INSERT INTO transactions (transaction_id, address, amount, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Loop through transactions and add to batch
            for (TransactionDTO transaction: transactions){
                pstmt.setString(1, transaction.getTransactionId());
                pstmt.setString(2, transaction.getAddress());
                pstmt.setDouble(3, transaction.getAmount());
                pstmt.setString(4, transaction.getTimestamp());

                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            System.out.println("Successfully saved " + results.length + " transactions to the database");

            conn.close();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
