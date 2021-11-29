package managers;

import dataTypes.*;
import dataTypes.tuples.Triplet;
import exceptions.IncorrectUserInputException;

import java.sql.SQLException;
import java.util.List;

public interface LinesInterface {

    void updateReachable(List<LineName> lines, StopName stop, Time time) throws SQLException, IncorrectUserInputException;

    Triplet<StopName, Time, TimeDiff> updateCapacityAndGetPreviousStop(LineName line, StopName stop, Time time);

    void saveUpdatedLineSegments() throws SQLException;

    void clean();
}
