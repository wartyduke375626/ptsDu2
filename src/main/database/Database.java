package database;

import dataTypes.Time;
import dataTypes.tuples.Pair;
import dataTypes.*;
import dataTypes.tuples.Triplet;

import java.sql.*;
import java.util.*;

public class Database implements DatabaseInterface {

    private final String databaseUrl;
    private Connection connection = null;

    public Database(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public void startSession() throws SQLException {
        if (connection != null) throw new IllegalStateException("Previous session has not been ended yet.");
        connection = DriverManager.getConnection(databaseUrl);
        connection.setAutoCommit(false);
        System.out.println("Connection to SQLite has been established.");
    }

    @Override
    public void endSession() throws SQLException {
        if (connection == null) throw new IllegalStateException("Session has not been started.");
        connection.commit();
        connection.close();
        System.out.println("Connection to SQLite closed correctly.");
        connection = null;
    }

    @Override
    public Optional<List<LineName>> getStopData(StopName stopName) throws SQLException {
        if (connection == null) throw new IllegalStateException("Session has not been started.");

        PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_STOP_LINES_QUERY);
        preparedStatement.setString(1, stopName.toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        List<LineName> stopLines = new ArrayList<>();
        while (resultSet.next()) {
            stopLines.add(new LineName(resultSet.getString("lname")));
        }
        if (stopLines.isEmpty()) {
            preparedStatement = connection.prepareStatement(Queries.GET_STOP_NAME_QUERY);
            preparedStatement.setString(1, stopName.toString());
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return Optional.empty();
            return Optional.of(stopLines);
        }

        return Optional.of(stopLines);
    }

    @Override
    public Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> getLineFirstStopAndLineSegmentsData(LineName lineName) throws SQLException {
        if (connection == null) throw new IllegalStateException("Session has not been started.");

        PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_LINE_FIRST_STOP_AND_LID_QUERY);
        preparedStatement.setString(1, lineName.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) return Optional.empty();

        StopName firstStop = new StopName(resultSet.getString("sname"));
        int lid = resultSet.getInt("lid");

        preparedStatement = connection.prepareStatement(Queries.GET_LINE_SEGMENTS_DATA_QUERY);
        preparedStatement.setInt(1, lid);
        resultSet = preparedStatement.executeQuery();

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

    @Override
    public Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> getBussesAndPassengers(LineName lineName, Time time, TimeDiff maxStartTimeDifference) throws SQLException {
        if (connection == null) throw new IllegalStateException("Session has not been started.");

        PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_BUSES_DATA_AND_BIDS_QUERY);
        preparedStatement.setString(1, lineName.toString());
        preparedStatement.setLong(2, time.getTime()+maxStartTimeDifference.getTime());
        preparedStatement.setLong(3, time.getTime()-maxStartTimeDifference.getTime());
        ResultSet resultSet = preparedStatement.executeQuery();

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
            preparedStatement = connection.prepareStatement(Queries.GET_BUS_SEGMENTS_PASSENGERS_QUERY);
            preparedStatement.setInt(1, busData.getFirst());
            resultSet = preparedStatement.executeQuery();

            List<Pair<Integer, Integer>> busSegmentsData = new ArrayList<>();
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

    @Override
    public void updateBusPassengers(Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> busesAndSegmentIndexesToUpdate) throws SQLException {
        if (connection == null) throw new IllegalStateException("Session has not been started.");

        Statement statement = connection.createStatement();

        for (Pair<LineName, Time> busToUpdate : busesAndSegmentIndexesToUpdate.keySet()) {
            LineName lineName = busToUpdate.getFirst();
            Time startTime = busToUpdate.getSecond();
            for (Pair<Integer, Integer> indexAndUpdateValue : busesAndSegmentIndexesToUpdate.get(busToUpdate)) {
                int sIndex = indexAndUpdateValue.getFirst();
                int newPassengers = indexAndUpdateValue.getSecond();

                PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_BID_AND_LSID_QUERY);
                preparedStatement.setLong(1, startTime.getTime());
                preparedStatement.setString(2, lineName.toString());
                preparedStatement.setInt(3, sIndex);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) throw new IllegalArgumentException("Argument data does not match a busSegment in database.");
                int bid = resultSet.getInt("bid");
                int lsid = resultSet.getInt("lsid");

                preparedStatement = connection.prepareStatement(Queries.UPDATE_BUS_SEGMENT_QUERY);
                preparedStatement.setInt(1, newPassengers);
                preparedStatement.setInt(2, bid);
                preparedStatement.setInt(3, lsid);
                preparedStatement.executeUpdate();

                if (resultSet.next()) throw new SQLIntegrityConstraintViolationException("Argument data matches multiple busSegments in database.");
            }
        }
    }

    @Override
    public void resetPassengers() throws SQLException {
        if (connection == null) throw new IllegalStateException("Session has not been started.");

        Statement statement = connection.createStatement();
        statement.executeUpdate(Queries.RESET_PASSENGERS_QUERY);
    }
}
