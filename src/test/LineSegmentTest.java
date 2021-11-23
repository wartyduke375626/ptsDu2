import components.LineSegment;
import components.StopInterface;
import dataTypes.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class LineSegmentTest {

    private LineSegment lineSegment;
    private final TimeDiff timeToNextStop = new TimeDiff(10);
    private StopInterface nextStop;
    private final int capacity = 1;
    private final LineName lineName = new LineName("L1");
    private final List<Time> startingTimes = List.of(new Time(10), new Time(20), new Time(30));
    private Pair<Optional<Time>, Optional<LineName>> nextStopData = new Pair<>(Optional.empty(), Optional.empty());

    @Before
    public void setUp() {
        nextStop = new StopInterface() {
            @Override
            public void updateReachableAt(Time time, LineName line) {
                nextStopData = new Pair<>(Optional.ofNullable(time), Optional.ofNullable(line));
            }

            @Override
            public Pair<Optional<Time>, Optional<LineName>> getReachableAt() {
                return nextStopData;
            }

            @Override
            public StopName getStopName() {
                return new StopName("Stop A");
            }

            @Override
            public List<LineName> getLines() {
                return List.of(new LineName("L1"), new LineName("L2"));
            }
        };
        lineSegment = new LineSegment(timeToNextStop, nextStop, capacity, lineName, startingTimes);
    }

    @Test
    public void nextStopTest() {
        Pair<Time, StopName> data = lineSegment.nextStop(new Time(10));
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond(), new StopName("Stop A"));
    }

    @Test
    public void nextStopAndUpdateReachableTest() {
        Triplet<Time, StopName, Boolean> data = lineSegment.nextStopAndUpdateReachable(new Time(10));
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond(), new StopName("Stop A"));
        assertTrue(data.getThird());
        assertTrue(nextStopData.getFirst().isPresent() && nextStopData.getSecond().isPresent());
        assertEquals(nextStopData.getFirst().get(), new Time(20));
        assertEquals(nextStopData.getSecond().get(), new LineName("L1"));
    }

    @Test
    public void incrementCapacityTest() {
        lineSegment.incrementCapacity(new Time(10));
        assertThrows(IllegalArgumentException.class, () -> lineSegment.incrementCapacity(new Time(10)));
        Triplet<Time, StopName, Boolean> data = lineSegment.nextStopAndUpdateReachable(new Time(10));
        assertFalse(data.getThird());
        assertTrue(nextStopData.getFirst().isEmpty() && nextStopData.getSecond().isEmpty());
    }
}
