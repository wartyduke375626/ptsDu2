package components;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import exceptions.IncorrectUserInputException;

import java.sql.SQLException;
import java.util.Map;

public interface LineSegmentInterface {

    Pair<Time, StopName> nextStop(Time startTime);

    Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) throws SQLException, IncorrectUserInputException;

    LineName getLine();

    int getSegmentIndex();

    TimeDiff getTimeDiffFromStart();

    void incrementCapacity(Time startTime);

    Map<Time, Integer> getUpdatedBusses();
}
