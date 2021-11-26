package managers;

import dataTypes.*;
import dataTypes.tuples.Triplet;

import java.util.List;

public interface LinesInterface {

    void updateReachable(List<LineName> lines, StopName stop, Time time);

    Triplet<StopName, Time, TimeDiff> updateCapacityAndGetPreviousStop(LineName line, StopName stop, Time time);

    void clean();
}
