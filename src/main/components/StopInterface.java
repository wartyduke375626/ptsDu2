package components;

import dataTypes.*;
import dataTypes.tuples.Pair;

import java.util.List;
import java.util.Optional;

public interface StopInterface {

    void updateReachableAt(Time time, LineName line);

    Pair<Time, Optional<LineName>> getReachableAt();

    StopName getStopName();

    List<LineName> getLines();
}
