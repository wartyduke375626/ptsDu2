import components.Stop;
import dataTypes.*;

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
    public void TestStop() {
        assertEquals(stopName, stop.getStopName());
        assertEquals(lines, stop.getLines());

        Pair<Optional<Time>, Optional<LineName>> data = stop.getReachableAt();
        assertTrue(data.getFirst().isEmpty() && data.getSecond().isEmpty());

        stop.updateReachableAt(new Time(20), new LineName("L5"));
        data = stop.getReachableAt();
        assertTrue(data.getFirst().isPresent() && data.getSecond().isPresent());
        assertEquals(data.getFirst().get(), new Time(20));
        assertEquals(data.getSecond().get(), new LineName("L5"));
    }
}
