package database;

import dataTypes.Time;
import dataTypes.tuples.Pair;
import dataTypes.*;
import dataTypes.tuples.Triplet;

import java.sql.*;
import java.util.*;

public class Database implements DatabaseInterface {

    private final String databaseUrl;

    public Database(String databasePath) {
        databaseUrl = "jdbc:sqlite:" + databasePath;
    }

    @Override
    public Optional<List<LineName>> getStopData(StopName stopName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Queries.getStopLinesQuery(stopName));

            List<LineName> stopLines = new ArrayList<>();
            while (resultSet.next()) {
                stopLines.add(new LineName(resultSet.getString("lname")));
            }
            if (stopLines.isEmpty()) return Optional.empty();

            return Optional.of(stopLines);
        }
    }

    @Override
    public Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> getLineFirstStopAndLineSegmentsData(LineName lineName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Queries.getLineFirstStopAndLidQuery(lineName));
            if (!resultSet.next()) return Optional.empty();

            StopName firstStop = new StopName(resultSet.getString("sname"));
            int lid = resultSet.getInt("lid");

            resultSet = statement.executeQuery(Queries.getLineSegmentsDataQuery(lid));

            List<Triplet<Integer, StopName, TimeDiff>> lineSegmentsData = new ArrayList<>();
            while (resultSet.next()) {
                int segmentIndex = resultSet.getInt("sIndex");
                StopName nextStop = new StopName(resultSet.getString("sname"));
                TimeDiff timeDiff = new TimeDiff(resultSet.getLong("timeDiff"));
                lineSegmentsData.add(new Triplet<>(segmentIndex, nextStop, timeDiff));
            }
            if (lineSegmentsData.isEmpty()) throw new SQLIntegrityConstraintViolationException("Line with no line segments in database.");

            return Optional.of(new Pair<>(firstStop, lineSegmentsData));
        }
    }

    @Override
    public Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> getBussesAndPassengers(LineName lineName, Time time, TimeDiff maxStartTimeDifference) throws SQLException {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Queries.getBusesDataAndBidsQuery(lineName, time, maxStartTimeDifference));

            List<Triplet<Integer, Time, Integer>> buses = new ArrayList<>();
            while (resultSet.next()) {
                int bid = resultSet.getInt("bid");
                Time startTime = new Time(resultSet.getLong("startTime"));
                int capacity = resultSet.getInt("capacity");
                buses.add(new Triplet<>(bid, startTime, capacity));
            }
            if (buses.isEmpty()) return Optional.empty();

            Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>> result = new HashMap<>();
            for (Triplet<Integer, Time, Integer> busData : buses) {
                List<Pair<Integer, Integer>> busSegmentsData = new ArrayList<>();
                resultSet = statement.executeQuery(Queries.getBusSegmentsPassengersQuery(busData.getFirst()));
                while (resultSet.next()) {
                    int segmentIndex = resultSet.getInt("sIndex");
                    int passengers = resultSet.getInt("passengers");
                    busSegmentsData.add(new Pair<>(segmentIndex, passengers));
                }
                if (busSegmentsData.isEmpty()) throw new SQLIntegrityConstraintViolationException("Bus with no bus segments in database.");
                result.put(busData.getSecond(), new Pair<>(busData.getThird(), busSegmentsData));
            }

            return Optional.of(result);
        }
    }

    @Override
    public void updateBusPassengers(Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> busesAndSegmentIndexesToUpdate) throws SQLException {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();

            for (Pair<LineName, Time> busToUpdate : busesAndSegmentIndexesToUpdate.keySet()) {
                LineName lineName = busToUpdate.getFirst();
                Time startTime = busToUpdate.getSecond();
                for (Pair<Integer, Integer> indexAndUpdateValue : busesAndSegmentIndexesToUpdate.get(busToUpdate)) {
                    int sIndex = indexAndUpdateValue.getFirst();
                    int newPassengers = indexAndUpdateValue.getSecond();

                    ResultSet resultSet = statement.executeQuery(Queries.getBIDAndLSIDQuery(startTime, lineName, sIndex));
                    if (!resultSet.next()) throw new IllegalArgumentException("Argument data does not match a busSegment in database.");
                    int bid = resultSet.getInt("bid");
                    int lsid = resultSet.getInt("lsid");
                    statement.executeUpdate(Queries.updateBusSegmentQuery(bid, lsid, newPassengers));

                    if (resultSet.next()) throw new SQLIntegrityConstraintViolationException("Argument data matches multiple busSegments in database.");
                }
            }
        }
    }
}
