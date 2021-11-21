package components;

import dataTypes.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

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
        Time catchableAt = startingTimes.get(0);
        int i = 1;
        while (catchableAt.compareTo(time) < 0) {
            if (i >= startingTimes.size()) return;
            catchableAt = startingTimes.get(i);
            i++;
        }

        StopName nextStop = firstStop;
        i = 0;
        while (!nextStop.equals(stop)) {
            if (i >= lineSegments.size()) throw new NoSuchElementException("No such stop in lineSegments");
            Pair<Time, StopName> data = lineSegments.get(i).nextStop(catchableAt);
            catchableAt = data.getFirst();
            nextStop = data.getSecond();
            i++;
        }

        while (i < lineSegments.size()) {
            Triplet<Time, StopName, Boolean> data = lineSegments.get(i).nextStopAndUpdateReachable(catchableAt);
            if (!data.getThird()) return;
            catchableAt = data.getFirst();
            nextStop = data.getSecond();
        }
    }

    @Override
    public StopName updateCapacityAndGetPreviousStop(StopName stop, Time time) {
        int i = 0;
        StopName nextStop = firstStop;
        Time startTime = startingTimes.get(0);
        while (!nextStop.equals(stop)) {
            i++;
            if (i >= lineSegments.size()) throw new NoSuchElementException("No such stop in lineSegments");
            Pair<Time, StopName> data = lineSegments.get(i).nextStop(startTime);
            startTime = data.getFirst();
            nextStop = data.getSecond();
        }

        lineSegments.get(i).incrementCapacity(time);
        return null;
    }
}
