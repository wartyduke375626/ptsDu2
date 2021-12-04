package components;

import dataTypes.*;
import dataTypes.tuples.Pair;
import exceptions.IncorrectUserInputException;
import managers.StopsInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class StopProxy implements StopInterface {

    private StopInterface stop = null;
    private final StopsInterface stops;
    private final StopName stopName;

    public StopProxy(StopsInterface stops, StopName stopName) {
        this.stops = stops;
        this.stopName = new StopName(stopName);
    }

    @Override
    public void updateReachableAt(Time time, LineName line) throws SQLException, IncorrectUserInputException {
        if (!stops.isLoaded(stopName)) {
            stops.loadStop(stopName);
            stop = stops.getStop(stopName);
        }
        else if (stop == null) stop = stops.getStop(stopName);
        stop.updateReachableAt(time, line);
    }

    @Override
    public Pair<Time, Optional<LineName>> getReachableAt() {
        if (!stops.isLoaded(stopName)) throw new IllegalStateException("Stop is not loaded");
        if (stop == null) throw new NoSuchElementException("Stop has not been loaded yet.");
        return stop.getReachableAt();
    }

    @Override
    public StopName getStopName() {
        return stopName;
    }

    @Override
    public List<LineName> getLines() throws SQLException, IncorrectUserInputException {
        if (!stops.isLoaded(stopName)) {
            stops.loadStop(stopName);
            stop = stops.getStop(stopName);
        }
        else if (stop == null) stop = stops.getStop(stopName);
        return stop.getLines();
    }
}
