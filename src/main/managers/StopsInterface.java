package managers;

import dataTypes.*;

import java.util.List;
import java.util.Optional;

public interface StopsInterface {

    Optional<Pair<StopName, Time>> earliestReachableStopAfter(Time time);

    void setStartingStop(StopName stop, Time time);

    List<LineName> getLines(StopName stop);

    Pair<Optional<Time>, Optional<LineName>> getReachableAt(StopName stop);
}
