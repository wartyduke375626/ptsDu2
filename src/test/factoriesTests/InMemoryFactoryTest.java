package factoriesTests;

import components.LineInterface;
import components.LineSegmentInterface;
import components.StopInterface;
import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import exceptions.IncorrectUserInputException;
import factories.InMemoryFactory;

import java.sql.SQLException;
import java.util.*;

import managers.Stops;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryFactoryTest {

    private InMemoryFactory inMemoryFactory;
    private LineSegmentInterface updateLineSegment;

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
        updateLineSegment = new LineSegmentInterface() {
            @Override
            public Pair<Time, StopName> nextStop(Time startTime) {
                return null;
            }

            @Override
            public Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) {
                return null;
            }

            @Override
            public LineName getLine() {
                return new LineName("L1");
            }

            @Override
            public int getSegmentIndex() {
                return 0;
            }

            @Override
            public TimeDiff getTimeDiffFromStart() {
                return null;
            }

            @Override
            public void incrementCapacity(Time startTime) {

            }

            @Override
            public Map<Time, Integer> getUpdatedBusses() {
                return Map.of(new Time(10), 8, new Time(20), 5);
            }
        };
    }

    @Test
    public void createStopTest() throws SQLException, IncorrectUserInputException {
        Optional<StopInterface> data = inMemoryFactory.createStop(new StopName("Stop B"));
        assertTrue(data.isPresent());
        assertEquals(data.get().getStopName(), new StopName("Stop B"));
        assertEquals(data.get().getLines(), inMemoryStops.get(new StopName("Stop B")));

        data = inMemoryFactory.createStop(new StopName("Stop D"));
        assertTrue(data.isEmpty());
    }

    @Test
    public void createLineTest() throws SQLException, IncorrectUserInputException {
        Optional<LineInterface> data = inMemoryFactory.createLine(new LineName("L1"), new Time(0));
        assertTrue(data.isPresent());
        LineInterface line = data.get();
        line.updateReachable(new Time(10), new StopName("Stop A"));
        line.updateReachable(new Time(20), new StopName("Stop B"));
        line.updateReachable(new Time(10), new StopName("Stop C"));
        line.updateReachable(new Time(10), new StopName("Stop B"));
        line.updateReachable(new Time(30), new StopName("Stop A"));
        assertEquals(line.updateCapacityAndGetPreviousStop(new StopName("Stop C"), new Time(30)).getFirst(), new StopName("Stop B"));
        assertThrows(NoSuchElementException.class, () -> line.updateCapacityAndGetPreviousStop(new StopName("Stop C"), new Time(10)));
        assertEquals(line.updateCapacityAndGetPreviousStop(new StopName("Stop B"), new Time(30)).getFirst(), new StopName("Stop A"));
        assertThrows(NoSuchElementException.class, () -> line.updateCapacityAndGetPreviousStop(new StopName("Stop A"), new Time(10)));
    }

    @Test
    public void updateDatabaseTest() {
        inMemoryFactory.updateDatabase(List.of(updateLineSegment));
    }
}
