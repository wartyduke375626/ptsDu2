package factoriesTests;

import components.LineInterface;
import components.StopInterface;
import dataTypes.*;
import factories.InMemoryFactory;

import java.util.*;

import managers.Stops;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryFactoryTest {

    private InMemoryFactory inMemoryFactory;
    private final Map<StopName, List<LineName>> inMemoryStops = Map.of(
            new StopName("Stop A"), List.of(new LineName("L1")),
            new StopName("Stop B"), List.of(new LineName("L1")),
            new StopName("Stop C"), List.of(new LineName("L1"))
    );
    private final Map<LineName, Triplet<List<Time>, StopName, Integer>> inMemoryLines = Map.of(
            new LineName("L1"), new Triplet<>(
                    List.of(new Time(10), new Time(20), new Time(30)),
                    new StopName("Stop A"),
                    10
            )
    );
    private final Map<LineName, List<Pair<StopName, TimeDiff>>> inMemoryLineSegments = Map.of(
            new LineName("L1"), List.of(
                    new Pair<>(new StopName("Stop B"), new TimeDiff(10)),
                    new Pair<>(new StopName("Stop C"), new TimeDiff(10))
            )
    );

    @Before
    public void setUp() {
        inMemoryFactory = new InMemoryFactory(inMemoryStops, inMemoryLines, inMemoryLineSegments);
        inMemoryFactory.setStops(new Stops(inMemoryFactory));
    }

    @Test
    public void createStopTest() {
        Optional<StopInterface> data = inMemoryFactory.createStop(new StopName("Stop B"));
        assertTrue(data.isPresent());
        assertEquals(data.get().getStopName(), new StopName("Stop B"));
        assertEquals(data.get().getLines(), inMemoryStops.get(new StopName("Stop B")));

        data = inMemoryFactory.createStop(new StopName("Stop D"));
        assertTrue(data.isEmpty());
    }

    @Test
    public void createLineTest() {
        Optional<LineInterface> data = inMemoryFactory.createLine(new LineName("L1"), new Time(0));
        assertTrue(data.isPresent());
        LineInterface line = data.get();
        line.updateReachable(new Time(10), new StopName("Stop A"));
        line.updateReachable(new Time(20), new StopName("Stop B"));
        line.updateReachable(new Time(10), new StopName("Stop C"));
        line.updateReachable(new Time(10), new StopName("Stop B"));
        line.updateReachable(new Time(30), new StopName("Stop A"));
        assertEquals(line.updateCapacityAndGetPreviousStop(new StopName("Stop C"), new Time(30)), new StopName("Stop B"));
        assertThrows(NoSuchElementException.class, () -> line.updateCapacityAndGetPreviousStop(new StopName("Stop C"), new Time(10)));
        assertEquals(line.updateCapacityAndGetPreviousStop(new StopName("Stop B"), new Time(30)), new StopName("Stop A"));
        assertThrows(NoSuchElementException.class, () -> line.updateCapacityAndGetPreviousStop(new StopName("Stop A"), new Time(10)));
    }
}
