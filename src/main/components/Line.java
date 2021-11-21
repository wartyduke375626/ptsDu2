package components;

import dataTypes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line implements LineInterface {

    private final LineName lineName;
    private final List<Time> startingTimes;
    private final StopName firstStop;
    private final List<LineSegmentInterface> lineSegments;

    public Line(LineName lineName, List<Time> startingTimes, StopName firstStop, List<LineSegmentInterface> lineSegments) {
        this.lineName = lineName;
        this.startingTimes = new ArrayList<>(startingTimes);
        Collections.sort(this.startingTimes);
        this.firstStop = firstStop;
        this.lineSegments = new ArrayList<>(lineSegments);
    }

    @Override
    public void updateReachable(Time time, StopName stop) {

    }

    @Override
    public StopName updateCapacityAndGetPreviousStop(StopName stop, Time time) {
        return null;
    }
}
