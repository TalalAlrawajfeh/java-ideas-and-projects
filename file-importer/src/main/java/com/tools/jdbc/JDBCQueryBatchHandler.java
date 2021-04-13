package com.tools.jdbc;

import java.sql.Connection;

public class JDBCQueryBatchHandler implements QueryBatchHandler {
	private JDBCQueryBatch jdbcQueryBatch;

	@Override
	public void createNewBatch(Connection connection, String tableName, String[] columnNames) throws Exception {
		jdbcQueryBatch = new JDBCQueryBatch(tableName, columnNames);
		jdbcQueryBatch.initializeQueryBatch(connection);
	}

	@Override
	public void addBatch(String[] values) throws Exception {
		jdbcQueryBatch.addQueryFromValues(values);
	}

	@Override
	public void closeBatch() throws Exception {
		if (jdbcQueryBatch != null) {
			jdbcQueryBatch.closeQueryBatch();
		}
	}
}
