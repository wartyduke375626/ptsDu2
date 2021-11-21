package components;

import dataTypes.*;

public interface LineSegmentInterface {

    Pair<Time, StopName> nextStop();

    Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime);

    void incrementCapacity(Time startTime);
}
