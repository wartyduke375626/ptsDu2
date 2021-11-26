package managers;

import dataTypes.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

        StopName tmpStop = from;
        Time tmpTime = time;
        while (!tmpStop.equals(to)) {
            List<LineName> stopLines = stops.getLines(tmpStop);
            lines.updateReachable(stopLines, tmpStop, time);
            Optional<Pair<StopName, Time>> data = stops.earliestReachableStopAfter(tmpTime);
            if (data.isEmpty()) throw new NoSuchElementException("No connection found.");
            tmpStop = data.get().getFirst();
            tmpTime = data.get().getSecond();
        }

        while (!tmpStop.equals(from)) {
            Pair<Time, Optional<LineName>> data = stops.getReachableAt(tmpStop);
            if (data.getSecond().isEmpty()) throw new NullPointerException("A stop other than starting stop was not reached by line.");
            result.addTravelSegment(data.getSecond().get(), tmpStop, data.getFirst());
            tmpStop = lines.updateCapacityAndGetPreviousStop(data.getSecond().get(), tmpStop, data.getFirst());
        }

        return result;
    }
}
