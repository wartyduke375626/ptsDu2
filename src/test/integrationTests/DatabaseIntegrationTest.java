package integrationTests;

import dataTypes.*;

import dataTypes.tuples.Quadruplet;
import database.Database;
import database.DatabaseInterface;
import factories.DatabaseFactory;
import factories.FactoryInterface;
import managers.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class DatabaseIntegrationTest {

    private static final TimeDiff MAX_START_TIME_DIFFERENCE = new TimeDiff(100);

    private ConnectionSearch connectionSearch;
    DatabaseInterface database;

    @Before
    public void setUp() {
        database = new Database(DBPathsAndURLs.TEST_DB_URL);
        FactoryInterface factory = new DatabaseFactory(database, MAX_START_TIME_DIFFERENCE);
        StopsInterface stops = new Stops(factory);
        LinesInterface lines = new Lines(factory);
        factory.setStops(stops);
        connectionSearch = new ConnectionSearch(stops, lines);
    }

    @Test
    public void searchTest1() throws SQLException {
        Optional<ConnectionData> data = connectionSearch.search(new StopName("STOP U"), new StopName("STOP O"), new Time(0));
        assertTrue(data.isEmpty());

        data = connectionSearch.search(new StopName("STOP F"), new StopName("STOP D"), new Time(20));
        assertTrue(data.isPresent());
        List<Quadruplet<LineName, StopName, Time, TimeDiff>> segmentsData = data.get().getTravelSegments();

        assertEquals(segmentsData.size(), 4);
        Quadruplet<LineName, StopName, Time, TimeDiff> x = segmentsData.get(0);
        assertEquals(x.getFirst(), new LineName("L2"));
        assertEquals(x.getSecond(), new StopName("STOP F"));
        assertEquals(x.getThird(), new Time(40));
        assertEquals(x.getForth(), new TimeDiff(8));
        x = segmentsData.get(1);
        assertEquals(x.getFirst(), new LineName("L2"));
        assertEquals(x.getSecond(), new StopName("STOP E"));
        assertEquals(x.getThird(), new Time(48));
        assertEquals(x.getForth(), new TimeDiff(5));
        x = segmentsData.get(2);
        assertEquals(x.getFirst(), new LineName("L2"));
        assertEquals(x.getSecond(), new StopName("STOP I"));
        assertEquals(x.getThird(), new Time(53));
        assertEquals(x.getForth(), new TimeDiff(2));
        x = segmentsData.get(3);
        assertEquals(x.getFirst(), new LineName("L5"));
        assertEquals(x.getSecond(), new StopName("STOP H"));
        assertEquals(x.getThird(), new Time(65));
        assertEquals(x.getForth(), new TimeDiff(4));
        assertEquals(data.get().getLastStop(), new StopName("STOP D"));

        database.startSession();
        database.resetPassengers();
        database.endSession();
    }

    @Test
    public void searchTest2() throws SQLException {
        Optional<ConnectionData> data = connectionSearch.search(new StopName("NO STOP"), new StopName("NO STOP"), new Time(0));
        assertTrue(data.isEmpty());

        for (int i=0; i<5; i++) {
            data = connectionSearch.search(new StopName("STOP A"), new StopName("STOP D"), new Time(20));
            assertTrue(data.isPresent());
            data = connectionSearch.search(new StopName("STOP A"), new StopName("STOP D"), new Time(50));
            assertTrue(data.isPresent());
            data = connectionSearch.search(new StopName("STOP A"), new StopName("STOP D"), new Time(80));
            assertTrue(data.isPresent());
        }
        //segment should now be fully booked

        data = connectionSearch.search(new StopName("STOP A"), new StopName("STOP D"), new Time(20));
        assertTrue(data.isEmpty());

        database.startSession();
        database.resetPassengers();
        database.endSession();
    }

}
