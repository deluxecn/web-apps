/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    private List<Connection> connectionPool = new ArrayList<Connection>();

    private String jdbcDriver;
    private String jdbcURL;
    private String tableName;

    public FavoriteDAO(String jdbcDriver, String jdbcURL, String tableName)
            throws MyDAOException {
        this.jdbcDriver = jdbcDriver;
        this.jdbcURL = jdbcURL;
        this.tableName = tableName;

        if (!tableExists())
            createTable();
    }

    private synchronized Connection getConnection() throws MyDAOException {
        if (connectionPool.size() > 0) {
            return connectionPool.remove(connectionPool.size() - 1);
        }

        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            throw new MyDAOException(e);
        }

        try {
            return DriverManager.getConnection(jdbcURL);
        } catch (SQLException e) {
            throw new MyDAOException(e);
        }
    }

    private synchronized void releaseConnection(Connection con) {
        connectionPool.add(con);
    }

    public void create(FavoriteBean item) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO "
                    + tableName
                    + " (userId, url, comment, count) VALUES (?,?,?,?)");
            pstmt.setInt(1, item.getUserId());
            pstmt.setString(2, item.getUrl());
            pstmt.setString(3, item.getComment());
            pstmt.setInt(4, item.getCount());
            pstmt.executeUpdate();
            pstmt.close();

            con.commit();
            con.setAutoCommit(true);

            releaseConnection(con);
            System.out.println("Added one item!");

        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    public void updateCount(int id) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " 
            + tableName + " WHERE id = " + id);
            rs.next();
            int newCount = rs.getInt("count") + 1;
            stmt.close();
            
            PreparedStatement pstmt = con.prepareStatement("UPDATE " + tableName
                    + " SET count = " + newCount + " WHERE id = " + id);
            pstmt.executeUpdate();
            pstmt.close();
            releaseConnection(con);
        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    public FavoriteBean[] getFavorites(int userId) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName
                    + " WHERE userId = " + userId);

            List<FavoriteBean> list = new ArrayList<FavoriteBean>();
            while (rs.next()) {
                FavoriteBean bean = new FavoriteBean();
                bean.setId(rs.getInt("id"));
                bean.setUserId(rs.getInt("userId"));
                bean.setUrl(rs.getString("url"));
                bean.setComment(rs.getString("comment"));
                bean.setCount(rs.getInt("count"));
                list.add(bean);
            }
            stmt.close();
            releaseConnection(con);

            return list.toArray(new FavoriteBean[list.size()]);
        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    private boolean tableExists() throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, null);

            boolean answer = rs.next();

            rs.close();
            releaseConnection(con);

            return answer;

        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    private void createTable() throws MyDAOException {
        Connection con = getConnection();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE " + tableName
                    + " (id INT NOT NULL AUTO_INCREMENT,"
                    + " userId INT NOT NULL," 
                    + " URL VARCHAR(255) NOT NULL,"
                    + " comment VARCHAR(255), "
                    + " count INT," 
                    + " PRIMARY KEY(id))");
            stmt.close();
            releaseConnection(con);
        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }
}
