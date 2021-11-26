import dataTypes.*;

import factories.FactoryInterface;
import factories.InMemoryFactory;
import managers.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class InMemoryIntegrationTest {

    private final Map<StopName, List<LineName>> inMemoryStops = Map.ofEntries(
            Map.entry(new StopName("Stop A"), List.of(new LineName("L1"))),
            Map.entry(new StopName("Stop B"), List.of(new LineName("L1"), new LineName("L3"))),
            Map.entry(new StopName("Stop C"), List.of(new LineName("L1"), new LineName("L2"), new LineName("L3"))),
            Map.entry(new StopName("Stop D"), List.of(new LineName("L1"), new LineName("L3"))),
            Map.entry(new StopName("Stop E"), List.of(new LineName("L1"))),
            Map.entry(new StopName("Stop F"), List.of(new LineName("L1"))),
            Map.entry(new StopName("Stop G"), List.of(new LineName("L2"))),
            Map.entry(new StopName("Stop H"), List.of(new LineName("L2"))),
            Map.entry(new StopName("Stop I"), List.of(new LineName("L2"))),
            Map.entry(new StopName("Stop J"), List.of(new LineName("L3"))),
            Map.entry(new StopName("Stop K"), List.of(new LineName("L3"))),
            Map.entry(new StopName("Stop L"), List.of(new LineName("L3")))
    );
    private final Map<LineName, Triplet<List<Time>, StopName, Integer>> inMemoryLines = Map.of(
            new LineName("L1"), new Triplet<>(
                    List.of(new Time(5), new Time(20), new Time(40), new Time(50)),
                    new StopName("Stop A"),
                    10
            ),
            new LineName("L2"), new Triplet<>(
            List.of(new Time(10), new Time(30), new Time(50)),
            new StopName("Stop G"),
                    5
            ),
            new LineName("L3"), new Triplet<>(
                    List.of(new Time(5), new Time(35), new Time(60)),
                    new StopName("Stop J"),
                    7
            )
    );
    private final Map<LineName, List<Pair<StopName, TimeDiff>>> inMemoryLineSegments = Map.of(
            new LineName("L1"), List.of(
                    new Pair<>(new StopName("Stop B"), new TimeDiff(4)),
                    new Pair<>(new StopName("Stop C"), new TimeDiff(3)),
                    new Pair<>(new StopName("Stop D"), new TimeDiff(4)),
                    new Pair<>(new StopName("Stop E"), new TimeDiff(3)),
                    new Pair<>(new StopName("Stop F"), new TimeDiff(5))
            ),
            new LineName("L2"), List.of(
                    new Pair<>(new StopName("Stop C"), new TimeDiff(3)),
                    new Pair<>(new StopName("Stop H"), new TimeDiff(3)),
                    new Pair<>(new StopName("Stop I"), new TimeDiff(5))
            ),
            new LineName("L3"), List.of(
                    new Pair<>(new StopName("Stop K"), new TimeDiff(7)),
                    new Pair<>(new StopName("Stop D"), new TimeDiff(4)),
                    new Pair<>(new StopName("Stop C"), new TimeDiff(4)),
                    new Pair<>(new StopName("Stop B"), new TimeDiff(3)),
                    new Pair<>(new StopName("Stop L"), new TimeDiff(2))
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
    public void searchTest() {
        ConnectionData data = connectionSearch.search(new StopName("Stop A"), new StopName("Stop D"), new Time(0));
        System.out.println(data);
    }
}
