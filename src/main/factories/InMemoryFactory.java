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
    private final Map<LineName, List<Map<Time, Integer>>> inMemoryLineSegmentsPassengers = new HashMap<>();

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
        initializeLineSegmentPassengers();
    }

    private void initializeLineSegmentPassengers() {
        for (LineName line : inMemoryLineSegments.keySet()) {
            List<Time> startingTimes = inMemoryLines.get(line).getFirst();
            List<Pair<StopName, TimeDiff>> lineSegmentsData = inMemoryLineSegments.get(line);
            TimeDiff totalTimeDiff = new TimeDiff(0);
            List<Map<Time, Integer>> lineSegmentsPassengers = new ArrayList<>();
            for (Pair<StopName, TimeDiff> lineSegmentData : lineSegmentsData) {
                Map<Time, Integer> lineSegmentPassengers = new HashMap<>();
                for (Time time : startingTimes) {
                    lineSegmentPassengers.put(new Time(time.getTime() + totalTimeDiff.getTime()), 0);
                }
                lineSegmentsPassengers.add(lineSegmentPassengers);
                totalTimeDiff = new TimeDiff(totalTimeDiff.getTime() + lineSegmentData.getSecond().getTime());
            }
            inMemoryLineSegmentsPassengers.put(line, lineSegmentsPassengers);
        }
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
        List<Map<Time, Integer>> lineSegmentsPassengers = inMemoryLineSegmentsPassengers.get(lineName);

        List<LineSegmentInterface> lineSegments = new ArrayList<>();
        for (int i=0; i<lineSegmentsData.size(); i++) {
            StopName stop = lineSegmentsData.get(i).getFirst();
            List<LineName> stopProxyLines = inMemoryStops.get(stop);
            StopProxy nextStop = new StopProxy(stops, stop, stopProxyLines);
            lineSegments.add(new LineSegment(lineSegmentsData.get(i).getSecond(), nextStop, lineData.getThird(), lineName, lineSegmentsPassengers.get(i), i));
        }

        return Optional.of(new Line(lineName, lineData.getFirst(), lineData.getSecond(), lineSegments));
    }

    @Override
    public void updateDatabase(List<LineSegmentInterface> lineSegments) {
        for (LineSegmentInterface lineSegment : lineSegments) {
            LineName lineName = lineSegment.getLine();
            for (Time time : lineSegment.getUpdatedBusses().keySet()) {
                Map<Time, Integer> inMemoryPassengers = inMemoryLineSegmentsPassengers.get(lineName).get(lineSegment.getSegmentIndex());
                if (!inMemoryPassengers.containsKey(time)) throw new NoSuchElementException("Bus to be updated not contained in memory.");
                inMemoryPassengers.put(time, lineSegment.getUpdatedBusses().get(time));
            }
        }
    }
}
