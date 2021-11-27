package componentsTests;

import components.LineSegment;
import components.StopInterface;
import dataTypes.*;

import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class LineSegmentTest {

    private LineSegment lineSegment;
    private final TimeDiff timeToNextStop = new TimeDiff(10);
    private StopInterface nextStop;
    private final int capacity = 1;
    private final LineName lineName = new LineName("L1");
    private final Map<Time, Integer> numberOfPassengers = Map.of(new Time(10), 0, new Time(20), 0, new Time(30), 0);
    private Pair<Time, Optional<LineName>> nextStopData = new Pair<>(new Time(Long.MAX_VALUE), Optional.empty());

    @Before
    public void setUp() {
        nextStop = new StopInterface() {
            @Override
            public void updateReachableAt(Time time, LineName line) {
                nextStopData = new Pair<>(time, Optional.ofNullable(line));
            }

            @Override
            public Pair<Time, Optional<LineName>> getReachableAt() {
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
        lineSegment = new LineSegment(timeToNextStop, nextStop, capacity, lineName, numberOfPassengers, 0);
    }

    @Test
    public void nextStopTest() {
        Pair<Time, StopName> data = lineSegment.nextStop(new Time(10));
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond(), new StopName("Stop A"));
    }

    @Test
    public void nextStopAndUpdateReachableTest() throws SQLException {
        Triplet<Time, StopName, Boolean> data = lineSegment.nextStopAndUpdateReachable(new Time(10));
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond(), new StopName("Stop A"));
        assertTrue(data.getThird());
        assertTrue(nextStopData.getSecond().isPresent());
        assertEquals(nextStopData.getFirst(), new Time(20));
        assertEquals(nextStopData.getSecond().get(), new LineName("L1"));
    }

    @Test
    public void incrementCapacityTest() throws SQLException {
        lineSegment.incrementCapacity(new Time(10));
        assertThrows(IllegalArgumentException.class, () -> lineSegment.incrementCapacity(new Time(10)));
        Triplet<Time, StopName, Boolean> data = lineSegment.nextStopAndUpdateReachable(new Time(10));
        assertFalse(data.getThird());
        assertTrue(nextStopData.getFirst().equals(new Time(Long.MAX_VALUE)) && nextStopData.getSecond().isEmpty());
    }
}
