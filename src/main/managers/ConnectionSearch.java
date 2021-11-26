package managers;

import dataTypes.*;

import java.util.*;

public class ConnectionSearch {

    private final StopsInterface stops;
    private final LinesInterface lines;

    public ConnectionSearch(StopsInterface stops, LinesInterface lines) {
        this.stops = stops;
        this.lines = lines;
    }

    public ConnectionData search(StopName from, StopName to, Time time) {
        ConnectionData result = new ConnectionData(from);
        stops.setStartingStop(from, time);

        List<StopName> earliestStops = new ArrayList<>(List.of(from));
        while (!earliestStops.contains(to)) {
            while (!earliestStops.isEmpty()) {
                StopName tmpStop = earliestStops.remove(earliestStops.size()-1);
                List<LineName> stopLines = stops.getLines(tmpStop);
                lines.updateReachable(stopLines, tmpStop, time);
            }
            Optional<Pair<List<StopName>, Time>> data = stops.earliestReachableStopAfter(time);
            if (data.isEmpty()) throw new NoSuchElementException("No connection found.");
            earliestStops.addAll(data.get().getFirst());
            time = data.get().getSecond();
        }

        StopName tmpStop = to;
        while (!tmpStop.equals(from)) {
            Pair<Time, Optional<LineName>> data = stops.getReachableAt(tmpStop);
            if (data.getSecond().isEmpty()) throw new NullPointerException("A stop other than starting stop was not reached by line.");
            result.addTravelSegment(data.getSecond().get(), tmpStop, data.getFirst());
            tmpStop = lines.updateCapacityAndGetPreviousStop(data.getSecond().get(), tmpStop, data.getFirst());
        }

        return result;
    }
}
