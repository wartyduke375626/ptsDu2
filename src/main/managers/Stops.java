package managers;

import components.StopInterface;
import dataTypes.*;
import dataTypes.tuples.Pair;
import exceptions.IncorrectUserInputException;
import factories.FactoryInterface;

import java.sql.SQLException;
import java.util.*;

public class Stops implements StopsInterface {

    private final FactoryInterface factory;
    private Map<StopName, StopInterface> stops = new HashMap<>();

    public Stops(FactoryInterface factory) {
        this.factory = factory;
    }

    @Override
    public Optional<Pair<List<StopName>, Time>> earliestReachableStopAfter(Time time) {
        Time min = new Time(Long.MAX_VALUE);
        for (StopName stop : stops.keySet()) {
            Pair<Time, Optional<LineName>> data = stops.get(stop).getReachableAt();
            if (data.getFirst().equals(new Time(Long.MAX_VALUE))) continue;

            Time reachable = data.getFirst();
            if (time.compareTo(reachable) < 0) {
                if (reachable.compareTo(min) < 0) min = reachable;
            }
        }

        if (min.equals(new Time(Long.MAX_VALUE))) return Optional.empty();
        List<StopName> earliestReachableStops = new ArrayList<>();
        for (StopName stop : stops.keySet()) {
            Pair<Time, Optional<LineName>> data = stops.get(stop).getReachableAt();
            if (data.getFirst().equals(min)) earliestReachableStops.add(stop);
        }
        return Optional.of(new Pair<>(earliestReachableStops, min));
    }

    @Override
    public void setStartingStop(StopName stop, Time time) throws SQLException, IncorrectUserInputException {
        if (!stops.containsKey(stop)) loadStop(stop);
        stops.get(stop).updateReachableAt(time, null);
    }

    @Override
    public List<LineName> getLines(StopName stop) throws SQLException, IncorrectUserInputException {
        if (!stops.containsKey(stop)) loadStop(stop);
        return stops.get(stop).getLines();
    }

    @Override
    public void loadStop(StopName stop) throws SQLException, IncorrectUserInputException {
        if (stops.containsKey(stop)) throw new IllegalStateException("Stop has already been loaded.");
        Optional<StopInterface> newStop = factory.createStop(stop);
        if (newStop.isEmpty()) throw new IncorrectUserInputException("No such stop in database.");
        stops.put(stop, newStop.get());
    }

    @Override
    public boolean isLoaded(StopName stop) {
        return stops.containsKey(stop);
    }

    @Override
    public StopInterface getStop(StopName stop) {
        if (!stops.containsKey(stop)) throw new NoSuchElementException("Stop has not been loaded yet.");
        return stops.get(stop);
    }

    @Override
    public Pair<Time, Optional<LineName>> getReachableAt(StopName stop) {
        if (!stops.containsKey(stop)) throw new IllegalStateException("Stop has not been loaded yet.");
        return stops.get(stop).getReachableAt();
    }

    @Override
    public void clean() {
        stops = new HashMap<>();
    }
}
