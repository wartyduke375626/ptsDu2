package components;

import dataTypes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineSegment implements LineSegmentInterface {

    private final TimeDiff timeToNextStop;
    private final int capacity;
    private final LineName lineName;
    private Map<Time, Integer> numberOfPassengers;

    public LineSegment(TimeDiff timeToNextStop, int capacity, LineName lineName, List<Time> startingTimes) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative.");
        this.timeToNextStop = timeToNextStop;
        this.capacity = capacity;
        this.lineName = lineName;
        this.numberOfPassengers = new HashMap<>();
        for (Time time : startingTimes) numberOfPassengers.put(time, 0);
    }

    @Override
    public Pair<Time, StopName> nextStop() {
        return null;
    }

    @Override
    public Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) {
        return null;
    }

    @Override
    public void incrementCapacity(Time startTime) {

    }
}
