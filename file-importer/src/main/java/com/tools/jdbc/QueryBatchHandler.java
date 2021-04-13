package com.tools.jdbc;

import java.sql.Connection;

public interface QueryBatchHandler {
	void createNewBatch(Connection connection, String tableName, String[] columnNames) throws Exception;

	void addBatch(String[] values) throws Exception;

	void closeBatch() throws Exception;
}
