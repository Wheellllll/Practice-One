package wheellllll.database;

import java.sql.*;

/**
 * This class provide methods for SQLite database.
 *
 * @author LiaoShanhe
 */
public class DatabaseUtils {

    /**
     * JDBC_DRIVER of SQLite & database url
     */
    private static String JDBC_DRIVER = "org.sqlite.JDBC";
    private static String DB_URL = "jdbc:sqlite:application.db";

    /**
     * This method create a connection to database use the <code>JDBC_DRIVER</code>
     * and <code>DB_URL</code>. The table will be created if it is not existing.
     *
     * @return Connection A connection to the database
     */
    private static Connection getConnection() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            String create_sql = "CREATE TABLE IF NOT EXISTS account (username VARCHAR , password VARCHAR )";
            statement.executeUpdate(create_sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * This method check whether a username exist in database.
     *
     * @param username The username that needs to check existence
     * @return boolean Return true if the username exist, otherwise false
     */
    public static boolean isExisted(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * This method check whether a username and password combination is existed in database.
     *
     * @param username Username of client
     * @param password Password of client, the password is encrypted with md5
     * @return boolean Return true if the username and password combination exists in database, otherwise false
     */
    public static boolean isValid(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * This method create a new account in database.
     *
     * @param username Username of client
     * @param password Password of client
     * @return boolean Return true if creating account successfully, otherwise false
     */
    public static boolean createAccount(String username, String password) {
        String sql = "INSERT INTO account VALUES (?, ?)";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
