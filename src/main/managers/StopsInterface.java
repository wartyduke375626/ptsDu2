package managers;

import components.StopInterface;
import dataTypes.*;

import java.util.List;
import java.util.Optional;

public interface StopsInterface {

    Optional<Pair<StopName, Time>> earliestReachableStopAfter(Time time);

    void setStartingStop(StopName stop, Time time);

    List<LineName> getLines(StopName stop);

    void loadStop(StopName stop);

    boolean isLoaded(StopName stop);

    StopInterface getStop(StopName stop);

    Pair<Time, Optional<LineName>> getReachableAt(StopName stop);

    void clean();
}
