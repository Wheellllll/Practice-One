package wheellllll.database;

import java.sql.*;

/**
 * This class provide methods for SQLite database.
 *
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
     * @return Connection to the database
     */
    private static Connection getConnection() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            String create_account_sql = "CREATE TABLE IF NOT EXISTS account " +
                    "(username VARCHAR PRIMARY KEY, password VARCHAR , groupid INT DEFAULT 1, lastupdate INT)";
            String create_message_sql = "CREATE TABLE IF NOT EXISTS message " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT,  message VARCHAR , fromuser VARCHAR , touser VARCHAR)";
            statement.executeUpdate(create_account_sql);
            statement.executeUpdate(create_message_sql);
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
     * @return Return the group id of the account if the username and password combination exists in database, otherwise -1
     */
    public static int isValid(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int groupId = resultSet.getInt("groupid");
                resultSet.close();
                return groupId;
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
        return -1;
    }

    /**
     * This method create a new account in database.
     *
     * @param username Username of client
     * @param password Password of client
     * @return boolean Return true if creating account successfully, otherwise false
     */
    public static boolean createAccount(String username, String password, int groupid) {
        String sql = "INSERT INTO account VALUES (?, ?, ?, NULL)";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, groupid);
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

    public static boolean createMessage(String message, String from, String to) {
        String sql = "INSERT INTO message VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, message);
            pstmt.setString(2, from);
            pstmt.setString(3, to);
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

    /**
     * This method change the group id in database of a user.
     * @param username Username of user
     * @param password Password of user
     * @param newGroupId New group id
     */
    public static boolean changeGroupId(String username, String password, int newGroupId) {
        String sql = "UPDATE account SET groupId = ? WHERE username = ? AND password = ?";
        PreparedStatement pstmt = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newGroupId);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
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
