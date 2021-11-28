package database;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DatabaseInterface {

    void startSession() throws SQLException;

    void endSession() throws SQLException;

    Optional<List<LineName>> getStopData(StopName stopName) throws SQLException;

    Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> getLineFirstStopAndLineSegmentsData(LineName lineName) throws SQLException;

    Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> getBussesAndPassengers(LineName lineName, Time time, TimeDiff maxStartTimeDifference) throws SQLException;

    void updateBusPassengers(Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> busesAndSegmentIndexesToUpdate) throws SQLException;
}
