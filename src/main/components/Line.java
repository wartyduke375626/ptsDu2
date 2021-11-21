package components;

import dataTypes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line implements LineInterface {

    private final LineName lineName;
    private final List<Time> startingTimes;
    private final StopName firstStop;

    public Line(LineName lineName, List<Time> startingTimes, StopName firstStop) {
        this.lineName = lineName;
        this.startingTimes = new ArrayList<>(startingTimes);
        Collections.sort(this.startingTimes);
        this.firstStop = firstStop;
    }

    @Override
    public void updateReachable(Time time, StopName stop) {

    }

    @Override
    public StopName updateCapacityAndGetPreviousStop(StopName stop, Time time) {
        return null;
    }
}
