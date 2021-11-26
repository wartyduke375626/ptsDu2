package dataTypes;

import java.util.ArrayList;
import java.util.List;

public class ConnectionData {

    private final StopName startingStop;
    private final List<Triplet<LineName, StopName, Time>> travelSegments = new ArrayList<>();

    public ConnectionData(StopName startingStop) {
        this.startingStop = startingStop;
    }

    public void addTravelSegment(LineName lineName, StopName destination, Time arrivalTime) {
        travelSegments.add(new Triplet<>(lineName, destination, arrivalTime));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startingStop.toString());
        for (int i=travelSegments.size()-1; i>=0; i--) {
            Triplet<LineName, StopName, Time> data = travelSegments.get(i);
            sb.append("-->").append(data.getFirst().toString());
            sb.append("-->").append(data.getSecond().toString());
        }
        return sb.toString();
    }
}
