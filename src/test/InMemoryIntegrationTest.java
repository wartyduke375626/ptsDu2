import dataTypes.*;

import dataTypes.tuples.Pair;
import dataTypes.tuples.Quadruplet;
import dataTypes.tuples.Triplet;
import factories.FactoryInterface;
import factories.InMemoryFactory;
import managers.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class InMemoryIntegrationTest {

    private final TimeDiff constantTimeDiff = new TimeDiff(5);
    private final List<Time> startingTimes = List.of(new Time(10), new Time(20), new Time(30));
    private final int busCapacity = 1;

    private final Map<StopName, List<LineName>> inMemoryStops = Map.ofEntries(
            Map.entry(new StopName("Stop A"), List.of(new LineName("L1"), new LineName("L2"))),
            Map.entry(new StopName("Stop B"), List.of(new LineName("L1"))),
            Map.entry(new StopName("Stop C"), List.of(new LineName("L1"), new LineName("L2"))),
            Map.entry(new StopName("Stop D"), List.of(new LineName("L2"))),
            Map.entry(new StopName("Stop X"), List.of()),
            Map.entry(new StopName("Stop Y"), List.of())
    );
    private final Map<LineName, Triplet<List<Time>, StopName, Integer>> inMemoryLines = Map.of(
            new LineName("L1"), new Triplet<>(
                    startingTimes,
                    new StopName("Stop A"),
                    busCapacity
            ),
            new LineName("L2"), new Triplet<>(
                    startingTimes,
                    new StopName("Stop C"),
                    busCapacity
            )
    );
    private final Map<LineName, List<Pair<StopName, TimeDiff>>> inMemoryLineSegments = Map.of(
            new LineName("L1"), List.of(
                    new Pair<>(new StopName("Stop B"), constantTimeDiff),
                    new Pair<>(new StopName("Stop C"), constantTimeDiff),
                    new Pair<>(new StopName("Stop X"), constantTimeDiff)
            ),
            new LineName("L2"), List.of(
                    new Pair<>(new StopName("Stop D"), constantTimeDiff),
                    new Pair<>(new StopName("Stop A"), constantTimeDiff),
                    new Pair<>(new StopName("Stop Y"), constantTimeDiff)
            )
    );
    private ConnectionSearch connectionSearch;

    @Before
    public void setUp() {
        FactoryInterface factory = new InMemoryFactory(inMemoryStops, inMemoryLines, inMemoryLineSegments);
        StopsInterface stops = new Stops(factory);
        LinesInterface lines = new Lines(factory);
        factory.setStops(stops);
        connectionSearch = new ConnectionSearch(stops, lines);
    }

    @Test
    public void searchTest1() {
        assertThrows(NoSuchElementException.class, () -> connectionSearch.search(new StopName("Stop I"), new StopName("Stop O"), new Time(0)));
        ConnectionData data = connectionSearch.search(new StopName("Stop A"), new StopName("Stop D"), new Time(0));
        assertEquals(data.getLastStop(), new StopName("Stop D"));
        List<Quadruplet<LineName, StopName, Time, TimeDiff>> segmentsData = data.getTravelSegments();
        assertEquals(segmentsData.size(), 3);
        Quadruplet<LineName, StopName, Time, TimeDiff> x = segmentsData.get(0);
        assertEquals(x.getFirst(), new LineName("L1"));
        assertEquals(x.getSecond(), new StopName("Stop A"));
        assertEquals(x.getThird(), new Time(10));
        assertEquals(x.getForth(), new TimeDiff(5));
        x = segmentsData.get(1);
        assertEquals(x.getFirst(), new LineName("L1"));
        assertEquals(x.getSecond(), new StopName("Stop B"));
        assertEquals(x.getThird(), new Time(15));
        assertEquals(x.getForth(), new TimeDiff(5));
        x = segmentsData.get(2);
        assertEquals(x.getFirst(), new LineName("L2"));
        assertEquals(x.getSecond(), new StopName("Stop C"));
        assertEquals(x.getThird(), new Time(20));
        assertEquals(x.getForth(), new TimeDiff(5));
    }

    @Test
    public void searchTest2() {
        ConnectionData data = connectionSearch.search(new StopName("Stop Y"), new StopName("Stop A"), new Time(0));
        assertNull(data);
        connectionSearch.search(new StopName("Stop A"), new StopName("Stop D"), new Time(0));
        data = connectionSearch.search(new StopName("Stop A"), new StopName("Stop D"), new Time(0));
        assertNotNull(data);
        data = connectionSearch.search(new StopName("Stop A"), new StopName("Stop D"), new Time(0));
        assertNull(data);
    }
}
