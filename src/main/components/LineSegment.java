package components;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;

import java.sql.SQLException;
import java.util.*;

public class LineSegment implements LineSegmentInterface {

    private final TimeDiff timeToNextStop;
    private final TimeDiff timeDiffFromStart;
    private final StopInterface nextStop;
    private final int capacity;
    private final LineName lineName;
    private final Map<Time, Integer> numberOfPassengers;
    private final Map<Time, Integer> updatedBusses = new HashMap<>();
    private final int segmentIndex;

    public LineSegment(TimeDiff timeToNextStop, TimeDiff timeDiffFromStart, StopInterface nextStop, int capacity, LineName lineName, Map<Time, Integer> numberOfPassengers, int segmentIndex) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative.");
        this.timeToNextStop = timeToNextStop;
        this.timeDiffFromStart = timeDiffFromStart;
        this.nextStop = nextStop;
        this.capacity = capacity;
        this.lineName = lineName;
        this.numberOfPassengers = new HashMap<>(numberOfPassengers);
        this.segmentIndex = segmentIndex;
    }

    @Override
    public Pair<Time, StopName> nextStop(Time startTime) {
        if (!numberOfPassengers.containsKey(startTime)) throw new NoSuchElementException("No match for bus at startTime.");
        Time time = new Time(timeToNextStop.getTime() + startTime.getTime());
        return new Pair<>(time, nextStop.getStopName());
    }

    @Override
    public Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) throws SQLException {
        if (!numberOfPassengers.containsKey(startTime)) throw new NoSuchElementException("No match for bus at startTime.");
        Time time = new Time(timeToNextStop.getTime() + startTime.getTime());
        boolean isFree = (numberOfPassengers.get(startTime) < capacity);
        if (isFree) nextStop.updateReachableAt(time, lineName);
        return new Triplet<>(time, nextStop.getStopName(), isFree);
    }

    @Override
    public LineName getLine() {
        return lineName;
    }

    @Override
    public int getSegmentIndex() {
        return segmentIndex;
    }

    @Override
    public TimeDiff getTimeDiffFromStart() {
        return timeDiffFromStart;
    }

    @Override
    public void incrementCapacity(Time startTime) {
        if (!numberOfPassengers.containsKey(startTime)) throw new NoSuchElementException("No match for bus at startTime.");
        if (numberOfPassengers.get(startTime) >= capacity) throw new IllegalArgumentException("Bus at startTime is full.");
        numberOfPassengers.put(startTime, numberOfPassengers.get(startTime) + 1);
        updatedBusses.put(startTime, numberOfPassengers.get(startTime));
    }

    @Override
    public Map<Time, Integer> getUpdatedBusses() {
        return updatedBusses;
    }

}
