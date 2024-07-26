package org.stegra.liquibase.issue6078;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public abstract class Demo {
	private static final Logger logger = Logger.getLogger(Demo.class.getName());
	private static final String LIQUIBASE_CHANGELOG = "liquibase.xml";

	public void execute(final String connectionUrl) throws Exception {
		System.out.println("Demo for LiquibaseIssue6078");

		Connection connection = DriverManager.getConnection(connectionUrl);
		initDatase(connection);

		testSelect(connection);
		executeLiquibase(connection);

		connection = DriverManager.getConnection(connectionUrl);
		testSelect(connection);

		connection.close();
	}

	private void initDatase(final Connection connection) throws SQLException {
		final Statement statement = connection.createStatement();
		statement.execute("drop table if exists country");
		statement
				.execute("create table country(id int primary key, name text, alpha_2 varchar(2), alpha_3 varchar(3))");
		statement.execute("insert into country values(9999, 'CountryX', 'XX', 'XXX')");
		statement.close();
	}

	private void executeLiquibase(final Connection connection) {
		try (JdbcConnection jdbcConnection = new JdbcConnection(connection);
				final Database database = DatabaseFactory.getInstance()
						.findCorrectDatabaseImplementation(jdbcConnection);) {
			Scope.child(
					Map.of(Scope.Attr.resourceAccessor.name(),
							new ClassLoaderResourceAccessor(Postgresql.class.getClassLoader())),
					() -> new CommandScope("update")
							.addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database)
							.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, LIQUIBASE_CHANGELOG)
							.addArgumentValue(DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS,
									new ChangeLogParameters(database))
							.execute());
		} catch (final Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
		}
	}

	private void testSelect(final Connection connection) throws SQLException {
		final Statement statement = connection.createStatement();
		ResultSet resultSet;
		resultSet = statement.executeQuery("select * from country");
		while (resultSet.next()) {
			System.out.println(resultSet.getString("name"));
		}

		statement.close();
	}
}
