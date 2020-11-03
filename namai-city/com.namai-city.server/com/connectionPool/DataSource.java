package com.connectionPool;

import java.sql.*;

public class DataSource {
	
	private static JDBCConnectionPool connectionPool;
	
	public DataSource() throws ClassNotFoundException, SQLException {
		connectionPool = new JDBCConnectionPool();
	}
	
	public static Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}
	
	public static void releaseConnection(Connection c) throws SQLException {
		connectionPool.releaseConnection(c);
	}
	
	public static void closeConnections() throws SQLException {
		connectionPool.closeConnections();
	}

	public static int getSize() {
		return connectionPool.getSize();
	}
	

}
