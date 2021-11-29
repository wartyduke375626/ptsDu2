package factoriesTests;

import components.LineInterface;
import components.LineSegmentInterface;
import components.StopInterface;
import dataTypes.*;

import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import database.DatabaseInterface;
import exceptions.IncorrectUserInputException;
import factories.DatabaseFactory;
import managers.Stops;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class DatabaseFactoryTest {

    private DatabaseFactory databaseFactory;

    private final List<StopName> dbStops = List.of(new StopName("STOP A"), new StopName("STOP B"), new StopName("STOP C"), new StopName("STOP D"));
    private final LineName dbLine = new LineName("L1");


    private final List<LineName> stopData = List.of(new LineName("L1"));

    private final Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>> lineFirstStopAndLineSegmentsData = new Pair<>(
            new StopName("STOP A"),
            new ArrayList<>(List.of(
                    new Triplet<>(1, new StopName("STOP C"), new TimeDiff(5)),
                    new Triplet<>(0, new StopName("STOP B"), new TimeDiff(5)),
                    new Triplet<>(2, new StopName("STOP D"), new TimeDiff(5))
            ))
    );

    private final Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>> bussesAndPassengersData = Map.of(
            new Time(10), new Pair<>(2, new ArrayList<>(List.of(
                    new Pair<>(2, 0),
                    new Pair<>(1, 0),
                    new Pair<>(0, 0)
            ))),
            new Time(20), new Pair<>(2, new ArrayList<>(List.of(
                    new Pair<>(0, 0),
                    new Pair<>(2, 0),
                    new Pair<>(1, 0)
            ))),
            new Time(30), new Pair<>(2, new ArrayList<>(List.of(
                    new Pair<>(1, 0),
                    new Pair<>(2, 0),
                    new Pair<>(0, 0)
            )))
    );

    private Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> updateResult = null;
    private final Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> expectedUpdateResult = Map.of(
            new Pair<>(new LineName("L1"), new Time(10)),
            List.of(new Pair<>(0, 5))
    );
    LineSegmentInterface fakeLineSegment;

    @Before
    public void setUp() {
        databaseFactory = new DatabaseFactory(new DatabaseInterface() {
            @Override
            public void startSession() throws SQLException {

            }

            @Override
            public void endSession() throws SQLException {

            }

            @Override
            public Optional<List<LineName>> getStopData(StopName stopName) throws SQLException {
                if (dbStops.contains(stopName)) return Optional.of(stopData);
                return Optional.empty();

            }

            @Override
            public Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> getLineFirstStopAndLineSegmentsData(LineName lineName) throws SQLException {
                if (lineName.equals(dbLine)) return Optional.of(lineFirstStopAndLineSegmentsData);
                return Optional.empty();
            }

            @Override
            public Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> getBussesAndPassengers(LineName lineName, Time time, TimeDiff maxStartTimeDifference) throws SQLException {
                if (lineName.equals(dbLine)) return Optional.of(bussesAndPassengersData);
                return Optional.empty();
            }

            @Override
            public void updateBusPassengers(Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> busesAndSegmentIndexesToUpdate) throws SQLException {
                updateResult = busesAndSegmentIndexesToUpdate;
            }

            @Override
            public void resetPassengers() throws SQLException {

            }

        }, new TimeDiff(100));

        databaseFactory.setStops(new Stops(databaseFactory));

        fakeLineSegment = new LineSegmentInterface() {
            @Override
            public Pair<Time, StopName> nextStop(Time startTime) {
                return null;
            }

            @Override
            public Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime) throws SQLException {
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
                return new TimeDiff(0);
            }

            @Override
            public void incrementCapacity(Time startTime) {

            }

            @Override
            public Map<Time, Integer> getUpdatedBusses() {
                return Map.of(new Time(10), 5);
            }
        };
    }

    @Test
    public void createStopTest() throws SQLException, IncorrectUserInputException {
        Optional<StopInterface> data = databaseFactory.createStop(new StopName("STOP A"));
        assertTrue(data.isPresent());
        assertEquals(data.get().getStopName(), new StopName("STOP A"));
        assertEquals(data.get().getLines(), stopData);

        data = databaseFactory.createStop(new StopName("NO STOP"));
        assertTrue(data.isEmpty());
    }

    @Test
    public void createLineTest() throws SQLException, IncorrectUserInputException {
        Optional<LineInterface> data = databaseFactory.createLine(new LineName("L1"), new Time(0));
        assertTrue(data.isPresent());
        LineInterface line = data.get();
        line.updateReachable(new Time(10), new StopName("STOP A"));
        line.updateReachable(new Time(15), new StopName("STOP B"));
        line.updateReachable(new Time(20), new StopName("STOP C"));
        line.updateReachable(new Time(25), new StopName("STOP B"));
        line.updateReachable(new Time(30), new StopName("STOP A"));
        assertEquals(line.updateCapacityAndGetPreviousStop(new StopName("STOP C"), new Time(20)).getFirst(), new StopName("STOP B"));
        assertThrows(NoSuchElementException.class, () -> line.updateCapacityAndGetPreviousStop(new StopName("STOP C"), new Time(10)));
        assertEquals(line.updateCapacityAndGetPreviousStop(new StopName("STOP B"), new Time(15)).getFirst(), new StopName("STOP A"));
        assertThrows(NoSuchElementException.class, () -> line.updateCapacityAndGetPreviousStop(new StopName("STOP A"), new Time(10)));
    }

    @Test
    public void updateDatabaseTest() throws SQLException {
        databaseFactory.updateDatabase(List.of(fakeLineSegment));
        assertEquals(expectedUpdateResult, updateResult);
    }
}
