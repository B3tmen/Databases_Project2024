package org.unibl.etf.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ConnectionPool {

	private static final String BUNDLE_NAME = ConnectionPool.class.getName();

	private int preconnectCount;
	private int maxIdleConnections;
	private int maxConnections;

	private int connectCount;
	private List<Connection> usedConnections;
	private List<Connection> freeConnections;

	private static ConnectionPool instance;

	public static ConnectionPool getInstance() {
		if (instance == null)
			instance = new ConnectionPool();

		return instance;
	}

	private ConnectionPool() {
		readConnectionPoolPropertyConfiguration();

		try {
			freeConnections = new ArrayList<Connection>();
			usedConnections = new ArrayList<Connection>();

			for (int i = 0; i < preconnectCount; i++) {
				Connection conn = DatabaseConnection.getInstance().getConnection();
				freeConnections.add(conn);
			}
			connectCount = preconnectCount;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readConnectionPoolPropertyConfiguration() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle(BUNDLE_NAME);
		preconnectCount = 0;
		maxIdleConnections = 10;
		maxConnections = 10;

		try {
			preconnectCount = Integer.parseInt(bundle
					.getString("preconnectCount"));
			maxIdleConnections = Integer.parseInt(bundle
					.getString("maxIdleConnections"));
			maxConnections = Integer.parseInt(bundle
					.getString("maxConnections"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized Connection checkOut() throws SQLException {
		Connection conn = null;
		if (!freeConnections.isEmpty()) {
			conn = freeConnections.remove(0);
			usedConnections.add(conn);
		}
		else {
			if (connectCount < maxConnections) {
				conn = DatabaseConnection.getInstance().getConnection();
				usedConnections.add(conn);
				connectCount++;
			}
			else {
				try {
					wait();
					conn = freeConnections.remove(0);
					usedConnections.add(conn);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return conn;
	}

	public synchronized void checkIn(Connection conn) {
		if (conn == null)
			return;

		if (usedConnections.remove(conn)) {
			freeConnections.add(conn);
			while (freeConnections.size() > maxIdleConnections) {
				int lastOne = freeConnections.size() - 1;
				Connection c = freeConnections.remove(lastOne);
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			notify();
		}
	}

	public synchronized void closeStatement(Statement stmt){
		try{
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
