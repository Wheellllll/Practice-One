import java.sql.*;

/**
 * Created by LiaoShanhe on 2016/3/19.
 */
public class DatabaseUtils {

    private static String JDBC_DRIVER = "org.sqlite.JDBC";
    private static String DB_URL = "jdbc:sqlite:application.db";

    private static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

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

    /*public static void createTable() {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE account (username VARCHAR , password VARCHAR )");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

}
