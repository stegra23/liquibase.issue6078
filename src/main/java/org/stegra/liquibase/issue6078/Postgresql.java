package org.stegra.liquibase.issue6078;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Postgresql extends Demo {
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String JDBC_CONNECTION_URL = "jdbc:postgresql://localhost:5432/stegra2?user=stegra"
			+ "&password=stegra";
	private static final Logger logger = Logger.getLogger(Postgresql.class.getName());

	public static void main(final String[] args) {
		try {
			Class.forName(DRIVER);
			final Postgresql postgresql = new Postgresql();
			postgresql.execute(JDBC_CONNECTION_URL);
		} catch (final Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
		}
	}
}
