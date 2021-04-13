package com.tools.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import com.tools.beans.ConnectionSettings;

public class JDBCConnectionUtility {
	public static Connection getNewConnection(ConnectionSettings connectionSettings) throws Exception {
		Class.forName(connectionSettings.getDriverClassName());
		return DriverManager.getConnection(connectionSettings.getUrl(), connectionSettings.getUsername(),
				connectionSettings.getPassword());
	}
}
