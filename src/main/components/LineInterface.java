package components;

import dataTypes.*;
import dataTypes.tuples.Triplet;

public interface LineInterface {

    void updateReachable(Time time, StopName stop);

    Triplet<StopName, Time, TimeDiff> updateCapacityAndGetPreviousStop(StopName stop, Time time);
}
