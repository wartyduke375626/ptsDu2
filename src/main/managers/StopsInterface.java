package managers;

import dataTypes.*;

import java.util.List;
import java.util.Optional;

public interface StopsInterface {

    Optional<Pair<StopName, Time>> earliestReachableStopAfter();

    boolean setStartingStop(StopName stop, Time time);

    List<LineName> getLines(StopName stop);

    Pair<Time, LineName> getReachableAt(StopName stop);
}
