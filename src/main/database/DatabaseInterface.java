package database;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Quadruplet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DatabaseInterface {

    Optional<List<LineName>> getStopData(StopName stopName);

    Optional<Pair<StopName, List<Pair<StopName, TimeDiff>>>> getLineFirstStopAndSegmentsData(LineName lineName);

    Optional<Map<Time, Pair<Integer, List<Integer>>>> getBussesAndPassengers(LineName lineName, Time time);
}
