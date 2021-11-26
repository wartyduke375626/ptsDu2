package componentsTests;

import components.Stop;
import dataTypes.*;

import dataTypes.tuples.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class StopTest {

    private Stop stop;
    private final List<LineName> lines = List.of(new LineName("L1"), new LineName("L2"), new LineName("L3"));
    private final StopName stopName = new StopName("Stop A");

    @Before
    public void setUp() {
        stop = new Stop(stopName, lines);
    }

    @Test
    public void getMethodsTest() {
        assertEquals(stopName, stop.getStopName());
        assertEquals(lines, stop.getLines());

        Pair<Time, Optional<LineName>> data = stop.getReachableAt();
        assertTrue(data.getFirst().equals(new Time(Long.MAX_VALUE)) && data.getSecond().isEmpty());
    }

    @Test
    public void updateReachableTest() {
        stop.updateReachableAt(new Time(20), new LineName("L5"));
        Pair<Time, Optional<LineName>> data = stop.getReachableAt();
        assertTrue(data.getSecond().isPresent());
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond().get(), new LineName("L5"));

        stop.updateReachableAt(new Time(30), new LineName("L2"));
        data = stop.getReachableAt();
        assertTrue(data.getSecond().isPresent());
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond().get(), new LineName("L5"));

        stop.updateReachableAt(new Time(20), new LineName("L3"));
        data = stop.getReachableAt();
        assertTrue(data.getSecond().isPresent());
        assertEquals(data.getFirst(), new Time(20));
        assertEquals(data.getSecond().get(), new LineName("L5"));
    }
}
