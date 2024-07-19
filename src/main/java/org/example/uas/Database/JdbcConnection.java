package org.example.uas.Database;
import java.sql.*;


public class JdbcConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/koperasi";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";
    private static Connection databaseLink;

    public static Connection getConnection(){
        try {
            databaseLink = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
    public static void closeConnection(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(PreparedStatement preparedStatement) {
        try {
            if(preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(ResultSet result, PreparedStatement statement) {
        try {
            if(statement != null) {
                statement.close();
            }

            if(result != null) {
                result.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Statement statement) {
        try {
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

