package components;

import dataTypes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class LineSegment implements LineSegmentInterface {

    private final TimeDiff timeToNextStop;
    private final StopInterface nextStop;
    private final int capacity;
    private final LineName lineName;
    private final Map<Time, Integer> numberOfPassengers = new HashMap<>();

    public LineSegment(TimeDiff timeToNextStop, StopInterface nextStop, int capacity, LineName lineName, List<Time> startingTimes) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative.");
        this.timeToNextStop = timeToNextStop;
        this.nextStop = nextStop;
        this.capacity = capacity;
        this.lineName = lineName;
        for (Time time : startingTimes) numberOfPassengers.put(time, 0);
    }

    @Override
    public Pair<Time, StopName> nextStop(Time startTime) {
        if (!numberOfPassengers.containsKey(startTime)) throw new NoSuchElementException("No match for bus at startTime.");
        Time time = new Time(timeToNextStop.getTime() + startTime.getTime());
        return new Pair<>(time, nextStop.getStopName());
    }

    @Override
    public Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) {
        if (!numberOfPassengers.containsKey(startTime)) throw new NoSuchElementException("No match for bus at startTime.");
        Time time = new Time(timeToNextStop.getTime() + startTime.getTime());
        boolean isFree = (numberOfPassengers.get(startTime) < capacity);
        if (isFree) nextStop.updateReachableAt(time, lineName);
        return new Triplet<>(time, nextStop.getStopName(), isFree);
    }

    @Override
    public void incrementCapacity(Time startTime) {
        if (!numberOfPassengers.containsKey(startTime)) throw new NoSuchElementException("No match for bus at startTime.");
        if (numberOfPassengers.get(startTime) >= capacity) throw new IllegalArgumentException("Bus at startTime is full.");
        numberOfPassengers.put(startTime, numberOfPassengers.get(startTime) + 1);
    }
}
