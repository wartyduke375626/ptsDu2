package components;

import dataTypes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stop implements StopInterface {

    private final StopName stopName;
    private final List<LineName> lines;
    private Time reachableAt = null;
    private LineName reachableVia = null;

    public Stop(StopName stopName, List<LineName> lines) {
        this.stopName = stopName;
        this.lines = Collections.unmodifiableList(lines);
    }

    @Override
    public void updateReachableAt(Time time, LineName line) {
        reachableAt = time;
        reachableVia = line;
    }

    @Override
    public Pair<Time, LineName> getReachableAt() {
        return new Pair<>(reachableAt, reachableVia);
    }

    @Override
    public StopName getStopName() {
        return stopName;
    }

    @Override
    public List<LineName> getLines() {
        return Collections.unmodifiableList(lines);
    }
}
