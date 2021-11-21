package components;

import dataTypes.*;

import java.util.*;

public class InMemoryStopFactory implements StopFactoryInterface {

    private final Map<StopName, List<LineName>> inMemoryStops;

    public InMemoryStopFactory(Map<StopName, List<LineName>> inMemoryStops) {
        this.inMemoryStops = new HashMap<>(inMemoryStops);
    }

    @Override
    public Optional<StopInterface> createStop(StopName stopName) {
        if (!inMemoryStops.containsKey(stopName)) return Optional.empty();
        else return Optional.of(new Stop(stopName, inMemoryStops.get(stopName)));
    }
}
