testDB:
	sqlite3 --init src/sql/tableDefinitions.sql testDB.db
	sqlite3 testDB.db < src/sql/insertTestValues.sql

clean:
	rm testDB.db
