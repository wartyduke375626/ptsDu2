package managers;

import components.StopInterface;
import dataTypes.*;
import factories.FactoryInterface;

import java.util.*;

public class Stops implements StopsInterface {

    private final FactoryInterface factory;
    private final Map<StopName, StopInterface> stops = new HashMap<>();

    public Stops(FactoryInterface factory) {
        this.factory = factory;
    }

    private void loadStop(StopName stop) {
        Optional<StopInterface> newStop = factory.createStop(stop);
        if (newStop.isEmpty()) throw new NoSuchElementException("No such stop in database.");
        stops.put(stop, newStop.get());
    }

    @Override
    public Optional<Pair<StopName, Time>> earliestReachableStopAfter(Time time) {
        Optional<Pair<StopName, Time>> result = Optional.empty();
        for (StopName stop : stops.keySet()) {
            Pair<Optional<Time>, Optional<LineName>> data = stops.get(stop).getReachableAt();
            if (data.getFirst().isEmpty()) continue;

            Time reachable = data.getFirst().get();
            if (time.compareTo(reachable) < 0) {
                if (result.isEmpty()) result = Optional.of(new Pair<>(stop, reachable));
                else if (reachable.compareTo(result.get().getSecond()) < 0) result = Optional.of(new Pair<>(stop, reachable));
            }
        }
        return result;
    }

    @Override
    public void setStartingStop(StopName stop, Time time) {
        if (!stops.containsKey(stop)) loadStop(stop);
        stops.get(stop).updateReachableAt(time, null);
    }

    @Override
    public List<LineName> getLines(StopName stop) {
        if (!stops.containsKey(stop)) loadStop(stop);
        return stops.get(stop).getLines();
    }

    @Override
    public StopInterface getStop(StopName stop) {
        if (!stops.containsKey(stop)) loadStop(stop);
        return stops.get(stop);
    }

    @Override
    public Pair<Optional<Time>, Optional<LineName>> getReachableAt(StopName stop) {
        if (!stops.containsKey(stop)) throw new NoSuchElementException("Stop has not been loaded yet.");
        return stops.get(stop).getReachableAt();
    }
}
