package factories;

import components.*;
import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import managers.StopsInterface;

import java.util.*;

public class InMemoryFactory implements FactoryInterface {

    private final Map<StopName, List<LineName>> inMemoryStops;
    private final Map<LineName, Triplet<List<Time>, StopName, Integer>> inMemoryLines;
    private final Map<LineName, List<Pair<StopName, TimeDiff>>> inMemoryLineSegments;

    private StopsInterface stops = null;

    public InMemoryFactory(
            Map<StopName, List<LineName>> inMemoryStops,
            Map<LineName, Triplet<List<Time>, StopName, Integer>> inMemoryLines,
            Map<LineName, List<Pair<StopName, TimeDiff>>> inMemoryLineSegments
    ) {
        if (!inMemoryLines.keySet().equals(inMemoryLineSegments.keySet())) throw new IllegalArgumentException("LineSegments do not mach with lines.");
        this.inMemoryStops = Collections.unmodifiableMap(inMemoryStops);
        this.inMemoryLines = Collections.unmodifiableMap(inMemoryLines);
        this.inMemoryLineSegments = Collections.unmodifiableMap(inMemoryLineSegments);
    }
    @Override
    public void setStops(StopsInterface stops) {
        this.stops = stops;
    }

    @Override
    public Optional<StopInterface> createStop(StopName stopName) {
        if (!inMemoryStops.containsKey(stopName)) return Optional.empty();
        return Optional.of(new Stop(stopName, inMemoryStops.get(stopName)));
    }

    @Override
    public Optional<LineInterface> createLine(LineName lineName, Time time) {
        if (stops == null) throw new IllegalStateException("Stops have not been initialized yet.");

        if (!inMemoryLines.containsKey(lineName)) return Optional.empty();
        Triplet<List<Time>, StopName, Integer> lineData = inMemoryLines.get(lineName);
        List<Pair<StopName, TimeDiff>> lineSegmentsData = inMemoryLineSegments.get(lineName);

        List<LineSegmentInterface> lineSegments = new ArrayList<>();
        TimeDiff totalTimeDiff = new TimeDiff(0);
        for (Pair<StopName, TimeDiff> lineSegmentData : lineSegmentsData) {
            List<LineName> stopProxyLines = inMemoryStops.get(lineSegmentData.getFirst());
            StopProxy nextStop = new StopProxy(stops, lineSegmentData.getFirst(), stopProxyLines);
            List<Time> startingTimes = new ArrayList<>();
            for (Time startTime : lineData.getFirst()) startingTimes.add(new Time(startTime.getTime() + totalTimeDiff.getTime()));
            lineSegments.add(new LineSegment(lineSegmentData.getSecond(), nextStop, lineData.getThird(), lineName, startingTimes));
            totalTimeDiff = new TimeDiff(totalTimeDiff.getTime() + lineSegmentData.getSecond().getTime());
        }

        return Optional.of(new Line(lineName, lineData.getFirst(), lineData.getSecond(), lineSegments));
    }
}
