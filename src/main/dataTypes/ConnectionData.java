package dataTypes;

import dataTypes.tuples.Quadruplet;

import java.util.ArrayList;
import java.util.List;

public class ConnectionData {

    private StopName lastStop;
    private final List<Quadruplet<LineName, StopName, Time, TimeDiff>> travelSegments = new ArrayList<>();

    public void setLastStop(StopName lastStop) {
        this.lastStop = lastStop;
    }

    public void addTravelSegment(LineName lineName, StopName startStop, Time startTime, TimeDiff travelTime) {
        travelSegments.add(new Quadruplet<>(lineName, startStop, startTime, travelTime));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=travelSegments.size()-1; i>=0; i--) {
            Quadruplet<LineName, StopName, Time, TimeDiff> data = travelSegments.get(i);
            sb.append("stop ").append(data.getSecond().toString());
            sb.append(", use line ").append(data.getFirst().toString());
            sb.append(", bus arrival at time ").append(data.getThird().toString());
            sb.append(", segment travel time ").append(data.getForth().toString());
            sb.append(";\n");
        }
        sb.append("stop ").append(lastStop.toString());
        sb.append(", destination;\n");
        return sb.toString();
    }
}
