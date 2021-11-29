package components;

import dataTypes.*;
import dataTypes.tuples.Triplet;
import exceptions.IncorrectUserInputException;

import java.sql.SQLException;
import java.util.List;

public interface LineInterface {

    void updateReachable(Time time, StopName stop) throws SQLException, IncorrectUserInputException;

    Triplet<StopName, Time, TimeDiff> updateCapacityAndGetPreviousStop(StopName stop, Time time);

    List<LineSegmentInterface> getLineSegments();

}
