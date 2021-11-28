db:
	sqlite3 --init src/sql/tableDefinitions.sql database.db
	sqlite3 database.db < src/sql/insertTestValues.sql
