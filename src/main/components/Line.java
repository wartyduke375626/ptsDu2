package components;

import dataTypes.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Line implements LineInterface {

    private final LineName lineName;
    private final List<Time> startingTimes;
    private final StopName firstStop;
    private final List<LineSegmentInterface> lineSegments;

    public Line(LineName lineName, List<Time> startingTimes, StopName firstStop, List<LineSegmentInterface> lineSegments) {
        if (startingTimes.size() == 0) throw new IllegalArgumentException("Line must have at least one bus.");
        if (lineSegments.size() == 0) throw new IllegalArgumentException("Line must have at least one segment.");
        this.lineName = lineName;
        this.startingTimes = Collections.unmodifiableList(startingTimes);
        this.firstStop = firstStop;
        this.lineSegments = Collections.unmodifiableList(lineSegments);
    }

    @Override
    public void updateReachable(Time time, StopName stop) {

        //find starting lineSegment and determine travel time from first stop till starting lineSegment
        StopName nextStop = firstStop;
        Time tmp = startingTimes.get(0);
        int startingLineSegmentIndex = 0;
        for (int i = 0; !Objects.equals(nextStop, stop); i++) {
            if (i >= lineSegments.size()) throw new NoSuchElementException("No such stop in lineSegments");
            Pair<Time, StopName> data = lineSegments.get(i).nextStop(tmp);
            tmp = data.getFirst();
            nextStop = data.getSecond();
            startingLineSegmentIndex++;
        }
        TimeDiff totalTimeDiff = new TimeDiff(tmp.getTime() - startingTimes.get(0).getTime());

        //determine earliest catchable bus at starting lineSegment
        Time earliestCatchable = new Time(startingTimes.get(0).getTime() + totalTimeDiff.getTime());
        for (int i=1; earliestCatchable.compareTo(time) < 0; i++) {
            if (i >= startingTimes.size()) return;
            earliestCatchable = new Time(startingTimes.get(i).getTime() + totalTimeDiff.getTime());
        }

        //update reachable stops from starting lineSegment
        while (startingLineSegmentIndex < lineSegments.size()) {
            Triplet<Time, StopName, Boolean> data = lineSegments.get(startingLineSegmentIndex).nextStopAndUpdateReachable(earliestCatchable);
            if (!data.getThird()) return;
            earliestCatchable = data.getFirst();
            startingLineSegmentIndex++;
        }
    }

    @Override
    public StopName updateCapacityAndGetPreviousStop(StopName stop, Time time) {
        if (stop.equals(firstStop)) throw new NoSuchElementException("No previous stop in line.");
        int i = 0;
        StopName nextStop = firstStop;
        StopName previousStop;
        Time startTime = startingTimes.get(0);
        do {
            if (i >= lineSegments.size()) throw new NoSuchElementException("No such stop in lineSegments");
            Pair<Time, StopName> data = lineSegments.get(i).nextStop(startTime);
            startTime = data.getFirst();
            previousStop = nextStop;
            nextStop = data.getSecond();
            i++;
        } while (!nextStop.equals(stop));

        lineSegments.get(--i).incrementCapacity(time);
        return previousStop;
    }
}
