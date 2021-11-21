package components;

import dataTypes.*;

public interface LineSegmentInterface {

    Pair<Time, StopName> nextStop(Time startTime);

    Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime);

    void incrementCapacity(Time startTime);
}
