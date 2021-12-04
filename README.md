# Transition search app

This project consists of a transition search simulation. The user can try to find a connection giving the start and destination stop and starting time. Finding an optimal connection will result in reserving a seat in all busses (only relevant travel segments) that will be used in the optimal connection.

The app has two versions:
- **in memory only**: by giving an initial set of data all changes will remain only until the app is shut down
- **persistent**: uses an SQLite database which contains all data and will persist after the app is shut down

## Ho to set up:
1. A Makefile is present in the project (UNIX only), which will create a testing database (testDB.db) using sqlite3 in the project root directory.
    - go into the project root directory, which contains the Makefile
    - use "make testDB" to create testing database (this database is used by database tests and will need to be present in the project root directory when testing)
    - use "make clean" to delete testDB.db file
    - if the testDB.db gets corrupted you can use "make testDB" anagin to recreate it
    - a diagram representing data generated in the testing database is present in the root directory of the project
2. For communication with the database server, JDBC is used, thus a JDBC driver for SQLite needs to be included in the project dependencies. You can download one from here: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc (version 3.36.0.3 was used when the project was tested) More info on SQLite JDBC can be found here: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/