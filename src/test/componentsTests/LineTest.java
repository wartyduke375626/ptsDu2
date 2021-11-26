package componentsTests;

import components.Line;
import components.LineSegmentInterface;
import dataTypes.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class LineTest {

    private Line line;
    private final LineName lineName = new LineName("L1");
    private final List<Time> startingTimes = List.of(new Time(10), new Time(20), new Time(30));
    private final StopName firstStop = new StopName("Stop A");
    private final List<StopName> nextStops = List.of(new StopName("Stop B"), new StopName("Stop C"));
    private final TimeDiff lineSegmentsTimeDiff = new TimeDiff(10);
    private final List<LineSegmentInterface> lineSegments = new ArrayList<>();

    private  List<Pair<Time, Optional<LineName>>> stopsData;
    private List<Boolean> lineSegmentCapacityUpdated;

    private void resetStopsData() {
        stopsData = new ArrayList<>();
        for (int i=0; i<nextStops.size()+1; i++) stopsData.add(new Pair<>(new Time(Long.MAX_VALUE), Optional.empty()));
    }

    private void resetLineSegmentCapacityUpdated() {
        lineSegmentCapacityUpdated = new ArrayList<>();
        for (int i=0; i<nextStops.size(); i++) lineSegmentCapacityUpdated.add(false);
    }

    @Before
    public void setUp() {
        for (int i=0; i<nextStops.size(); i++) {
            int finalI = i;
            lineSegments.add(new LineSegmentInterface() {
                @Override
                public Pair<Time, StopName> nextStop(Time startTime) {
                    Time time = new Time(startTime.getTime() + lineSegmentsTimeDiff.getTime());
                    return new Pair<>(time, nextStops.get(finalI));
                }

                @Override
                public Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) {
                    Time time = new Time(startTime.getTime() + lineSegmentsTimeDiff.getTime());
                    stopsData.set(finalI+1, new Pair<>(time, Optional.of(lineName)));
                    return new Triplet<>(time, nextStops.get(finalI), true);
                }

                @Override
                public void incrementCapacity(Time startTime) {
                    lineSegmentCapacityUpdated.set(finalI, true);
                }
            });
        }
        line = new Line(lineName, startingTimes, firstStop, lineSegments);
    }

    @Test
    public void updateReachableTest() {
        resetStopsData();
        line.updateReachable(new Time(0), new StopName("Stop A"));
        assertTrue(stopsData.get(0).getFirst().equals(new Time(Long.MAX_VALUE)) && stopsData.get(0).getSecond().isEmpty());
        assertTrue(stopsData.get(1).getSecond().isPresent());
        assertEquals(stopsData.get(1).getFirst(), new Time(20));
        assertEquals(stopsData.get(1).getSecond().get(), new LineName("L1"));
        assertTrue(stopsData.get(2).getSecond().isPresent());
        assertEquals(stopsData.get(2).getFirst(), new Time(30));
        assertEquals(stopsData.get(2).getSecond().get(), new LineName("L1"));

        resetStopsData();
        line.updateReachable(new Time(15), new StopName("Stop B"));
        assertTrue(stopsData.get(0).getFirst().equals(new Time(Long.MAX_VALUE)) && stopsData.get(0).getSecond().isEmpty());
        assertTrue(stopsData.get(1).getFirst().equals(new Time(Long.MAX_VALUE)) && stopsData.get(1).getSecond().isEmpty());
        assertTrue(stopsData.get(2).getSecond().isPresent());
        assertEquals(stopsData.get(2).getFirst(), new Time(30));
        assertEquals(stopsData.get(2).getSecond().get(), new LineName("L1"));

        resetStopsData();
        line.updateReachable(new Time(25), new StopName("Stop B"));
        assertTrue(stopsData.get(0).getFirst().equals(new Time(Long.MAX_VALUE)) && stopsData.get(0).getSecond().isEmpty());
        assertTrue(stopsData.get(1).getFirst().equals(new Time(Long.MAX_VALUE)) && stopsData.get(1).getSecond().isEmpty());
        assertTrue(stopsData.get(2).getSecond().isPresent());
        assertEquals(stopsData.get(2).getFirst(), new Time(40));
        assertEquals(stopsData.get(2).getSecond().get(), new LineName("L1"));
    }

    @Test
    public void updateCapacityAndGetPreviousStopTest() {
        resetLineSegmentCapacityUpdated();
        StopName stop = line.updateCapacityAndGetPreviousStop(new StopName("Stop B"), new Time(10));
        assertEquals(stop, new StopName("Stop A"));
        assertTrue(lineSegmentCapacityUpdated.get(0));
    }
}
