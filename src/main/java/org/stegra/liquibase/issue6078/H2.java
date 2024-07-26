package org.stegra.liquibase.issue6078;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.h2.tools.DeleteDbFiles;

public class H2 extends Demo {
	private static final String DRIVER = "org.h2.Driver";
	private static final String JDBC_CONNECTION_URL = "jdbc:h2:~/test";
	private static final Logger logger = Logger.getLogger(H2.class.getName());

	public static void main(final String[] args) {
		System.out.println("Demo for LiquibaseIssue6078");

		try {
			DeleteDbFiles.execute("~", "test", true);
			Class.forName(DRIVER);
			final H2 h2 = new H2();
			h2.execute(JDBC_CONNECTION_URL);
		} catch (final Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
		}
	}
}
