package factories;

import components.*;
import dataTypes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryFactory implements FactoryInterface {

    private final Map<StopName, List<LineName>> inMemoryStops;
    private final Map<LineName, Triplet<List<Time>, StopName, List<LineSegmentInterface>>> inMemoryLines;

    public InMemoryFactory(
            Map<StopName, List<LineName>> inMemoryStops,
            Map<LineName, Triplet<List<Time>, StopName, List<LineSegmentInterface>>> inMemoryLines)
    {
        this.inMemoryStops = new HashMap<>(inMemoryStops);
        this.inMemoryLines = new HashMap<>(inMemoryLines);
    }

    @Override
    public Optional<StopInterface> createStop(StopName stopName) {
        if (!inMemoryStops.containsKey(stopName)) return Optional.empty();
        else return Optional.of(new Stop(stopName, inMemoryStops.get(stopName)));
    }

    @Override
    public Optional<LineInterface> createLine(LineName lineName, Time time) {
        if (!inMemoryLines.containsKey(lineName)) return Optional.empty();
        else {
            Triplet<List<Time>, StopName, List<LineSegmentInterface>> data = inMemoryLines.get(lineName);
            return Optional.of(new Line(lineName, data.getFirst(), data.getSecond(), data.getThird()));
        }
    }
}
