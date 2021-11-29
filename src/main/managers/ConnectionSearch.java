package managers;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import exceptions.IncorrectUserInputException;

import java.sql.SQLException;
import java.util.*;

public class ConnectionSearch {

    private final StopsInterface stops;
    private final LinesInterface lines;

    public ConnectionSearch(StopsInterface stops, LinesInterface lines) {
        this.stops = stops;
        this.lines = lines;
    }

    public Optional<ConnectionData> search(StopName from, StopName to, Time time) {
        try {
            stops.setStartingStop(from, time);

            List<StopName> earliestStops = new ArrayList<>(List.of(from));
            while (!earliestStops.contains(to)) {
                while (!earliestStops.isEmpty()) {
                    StopName tmpStop = earliestStops.remove(earliestStops.size() - 1);
                    List<LineName> stopLines = stops.getLines(tmpStop);
                    lines.updateReachable(stopLines, tmpStop, time);
                }
                Optional<Pair<List<StopName>, Time>> data = stops.earliestReachableStopAfter(time);
                if (data.isEmpty()) {
                    System.out.println("No connection has been found");
                    return Optional.empty();
                }
                earliestStops.addAll(data.get().getFirst());
                time = data.get().getSecond();
            }

            ConnectionData result = new ConnectionData();
            StopName tmpStop = to;
            result.setLastStop(tmpStop);
            while (!tmpStop.equals(from)) {
                Pair<Time, Optional<LineName>> data = stops.getReachableAt(tmpStop);
                if (data.getSecond().isEmpty()) throw new NullPointerException("A stop other than starting stop was not reached by line.");

                Triplet<StopName, Time, TimeDiff> resultData = lines.updateCapacityAndGetPreviousStop(data.getSecond().get(), tmpStop, data.getFirst());
                result.addTravelSegment(data.getSecond().get(), resultData.getFirst(), resultData.getSecond(), resultData.getThird());
                tmpStop = resultData.getFirst();
            }

            lines.saveUpdatedLineSegments();

            return Optional.of(result);

        } catch (SQLException e) {
            System.err.println("Database fatal error: " + e.getMessage());
            return Optional.empty();

        } catch (IncorrectUserInputException e) {
            System.out.println("Incorrect user input: " + e.getMessage());
            return Optional.empty();

        } finally {
            lines.clean();
            stops.clean();
        }
    }
}
