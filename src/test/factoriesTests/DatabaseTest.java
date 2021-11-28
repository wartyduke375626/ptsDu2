package factoriesTests;

import dataTypes.*;

import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import database.Database;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class DatabaseTest {

    private static final String TEST_DB_PATH = "testDB.db";
    private static final TimeDiff MAX_START_TIME_DIFFERENCE = new TimeDiff(100);

    private Database database;

    private final Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> dataToBeUpdated = Map.of(
            new Pair<>(new LineName("L1"), new Time(10)),
            List.of(new Pair<>(1, 2), new Pair<>(2, 2), new Pair<>(3, 2)),
            new Pair<>(new LineName("L3"), new Time(35)),
            List.of(new Pair<>(0, 1), new Pair<>(1, 1), new Pair<>(2, 1))
    );

    @Before
    public void setUp() throws IOException, InterruptedException {
        database = new Database(TEST_DB_PATH);
    }

    @Test
    public void getStopDataTest() throws SQLException {
        database.startSession();

        Optional<List<LineName>> data = database.getStopData(new StopName("STOP D"));
        assertTrue(data.isPresent());
        List<LineName> expected = List.of(new LineName("L1"), new LineName("L4"), new LineName("L5"));
        data.get().sort(Comparator.comparing(LineName::toString));
        assertEquals(expected, data.get());

        data = database.getStopData(new StopName("NO STOP"));
        assertTrue(data.isEmpty());

        database.endSession();
    }

    @Test
    public void getLineFirstStopAndLineSegmentsDataTest() throws SQLException {
        database.startSession();

        Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> data = database.getLineFirstStopAndLineSegmentsData(new LineName("L1"));
        assertTrue(data.isPresent());
        StopName firstStop = data.get().getFirst();
        assertEquals(firstStop, new StopName("STOP B"));

        List<Triplet<Integer, StopName, TimeDiff>> expected = List.of(
                new Triplet<>(0, new StopName("STOP C"), new TimeDiff(7)),
                new Triplet<>(1, new StopName("STOP D"), new TimeDiff(4)),
                new Triplet<>(2, new StopName("STOP E"), new TimeDiff(3)),
                new Triplet<>(3, new StopName("STOP F"), new TimeDiff(8))
        );
        data.get().getSecond().sort(Comparator.comparing(Triplet::getFirst));
        assertEquals(expected, data.get().getSecond());

        data = database.getLineFirstStopAndLineSegmentsData(new LineName("NO LINE"));
        assertTrue(data.isEmpty());

        database.endSession();
    }

    @Test
    public void getBussesAndPassengersTest() throws SQLException {
        database.startSession();

        Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> data = database.getBussesAndPassengers(new LineName("L3"), new Time(50), MAX_START_TIME_DIFFERENCE);
        assertTrue(data.isPresent());
        Set<Time> expectedKeySet = Set.of(new Time(10), new Time(35), new Time(60), new Time(85));
        assertEquals(expectedKeySet, data.get().keySet());
        for (Time time : expectedKeySet) {
            Pair<Integer, List<Pair<Integer, Integer>>> busData = data.get().get(time);
            assertEquals(Integer.valueOf(7), busData.getFirst());
            List<Pair<Integer, Integer>> expectedSegmentsPassengers = List.of(
                    new Pair<>(0, 0),
                    new Pair<>(1, 0),
                    new Pair<>(2, 0),
                    new Pair<>(3, 0)
            );
            busData.getSecond().sort(Comparator.comparing(Pair::getFirst));
            assertEquals(expectedSegmentsPassengers, busData.getSecond());
        }

        data = database.getBussesAndPassengers(new LineName("NO LINE"), new Time(50), MAX_START_TIME_DIFFERENCE);
        assertTrue(data.isEmpty());
        data = database.getBussesAndPassengers(new LineName("L3"), new Time(Long.MAX_VALUE), MAX_START_TIME_DIFFERENCE);
        assertTrue(data.isEmpty());

        database.endSession();
    }

    @Test
    public void updateBusPassengersTest() throws SQLException {
        database.startSession();

        database.updateBusPassengers(dataToBeUpdated);
        Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> updatedLine = database.getBussesAndPassengers(new LineName("L1"), new Time(50), MAX_START_TIME_DIFFERENCE);
        assertTrue(updatedLine.isPresent());
        List<Pair<Integer, Integer>> bus = updatedLine.get().get(new Time(10)).getSecond();
        for (Pair<Integer, Integer> busSegment : bus) {
            if (busSegment.getFirst() == 0) assertEquals(busSegment.getSecond(), Integer.valueOf(0));
            else assertEquals(busSegment.getSecond(), Integer.valueOf(2));
        }

        updatedLine = database.getBussesAndPassengers(new LineName("L3"), new Time(50), MAX_START_TIME_DIFFERENCE);
        assertTrue(updatedLine.isPresent());
        bus = updatedLine.get().get(new Time(35)).getSecond();
        for (Pair<Integer, Integer> busSegment : bus) {
            if (busSegment.getFirst() == 3) assertEquals(busSegment.getSecond(), Integer.valueOf(0));
            else assertEquals(busSegment.getSecond(), Integer.valueOf(1));
        }

        database.resetPassengers();
        database.endSession();
    }
}
