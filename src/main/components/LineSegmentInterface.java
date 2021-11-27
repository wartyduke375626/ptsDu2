package components;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;

import java.util.Map;

public interface LineSegmentInterface {

    Pair<Time, StopName> nextStop(Time startTime);

    Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime);

    LineName getLine();

    int getSegmentIndex();

    void incrementCapacity(Time startTime);

    Map<Time, Integer> getUpdatedBusses();
}
