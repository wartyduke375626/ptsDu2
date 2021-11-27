package database;

import dataTypes.*;
import dataTypes.Time;
import dataTypes.tuples.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Database implements DatabaseInterface {

    private String databaseUrl;
    private static final String query =
            "SELECT l.lname " +
            "FROM stop s, line l, stop_line sl " +
            "WHERE s.sname = ? AND s.sid = sl.sid AND l.lid = sl.lid ";

    public Database(String databasePath) {
        databaseUrl = "jdbc:sqlite:" + databasePath;
    }

    @Override
    public Optional<List<LineName>> getStopData(StopName stopName) {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, stopName.toString());
            ResultSet resultSet = statement.executeQuery();
            List<LineName> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new LineName(resultSet.getString("lname")));
            }
            return Optional.of(result);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Pair<StopName, List<Pair<StopName, TimeDiff>>>> getLineFirstStopAndSegmentsData(LineName lineName) {
        return Optional.empty();
    }

    @Override
    public Optional<Map<Time, Pair<Integer, List<Integer>>>> getBussesAndPassengers(LineName lineName, Time time) {
        return Optional.empty();
    }
}
