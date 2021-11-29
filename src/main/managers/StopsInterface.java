package managers;

import components.StopInterface;
import dataTypes.*;
import dataTypes.tuples.Pair;
import exceptions.IncorrectUserInputException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StopsInterface {

    Optional<Pair<List<StopName>, Time>> earliestReachableStopAfter(Time time);

    void setStartingStop(StopName stop, Time time) throws SQLException, IncorrectUserInputException;

    List<LineName> getLines(StopName stop) throws SQLException, IncorrectUserInputException;

    void loadStop(StopName stop) throws SQLException, IncorrectUserInputException;

    boolean isLoaded(StopName stop);

    StopInterface getStop(StopName stop);

    Pair<Time, Optional<LineName>> getReachableAt(StopName stop);

    void clean();
}
