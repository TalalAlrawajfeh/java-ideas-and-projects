package com.tools.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JDBCQueryBatch {
	private static final int DEFAULT_MAX_STATEMENTS_COUNT = 100000;
	private String tableName;
	private String[] columnNames;
	private int maxStatementsCount;
	private int numberOfStatements;
	private Connection connection;
	private PreparedStatement preparedStatement;

	public JDBCQueryBatch(String tableName, String[] columnNames) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		maxStatementsCount = DEFAULT_MAX_STATEMENTS_COUNT;
	}

	public JDBCQueryBatch(String tableName, String[] columnNames, int maxStatementsCount) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.maxStatementsCount = maxStatementsCount;
	}

	public void addQueryFromValues(String[] values) throws Exception {
		for (int i = 1; i <= values.length; i++) {
			preparedStatement.setObject(i, values[i - 1]);
		}
		preparedStatement.addBatch();
		numberOfStatements++;
		if (numberOfStatements == maxStatementsCount) {
			executeAndCommitBatch();
		}
	}

	private void executeAndCommitBatch() throws Exception {
		int[] count = preparedStatement.executeBatch();
		for (int i : count) {
			if (i < 0) {
				throw new Exception("Some queries were not executed successfully");
			}
		}
		connection.commit();
		numberOfStatements = 0;
	}

	public void initializeQueryBatch(Connection connection) throws Exception {
		this.connection = connection;
		preparedStatement = connection.prepareStatement(prepareQuery());
		connection.setAutoCommit(false);
		numberOfStatements = 0;
	}

	public void closeQueryBatch() throws Exception {
		if (numberOfStatements > 0) {
			executeAndCommitBatch();
		}
		preparedStatement.close();
	}

	private String prepareQuery() {
		return "insert into " + tableName + "(" + Stream.of(columnNames).collect(Collectors.joining(",")) + ")values("
				+ Stream.of(columnNames).map(c -> "?").collect(Collectors.joining(",")) + ")";
	}
}
