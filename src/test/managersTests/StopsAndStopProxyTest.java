package managersTests;

import components.LineInterface;
import components.LineSegmentInterface;
import components.StopInterface;
import components.StopProxy;
import dataTypes.*;

import dataTypes.tuples.Pair;
import exceptions.IncorrectUserInputException;
import factories.FactoryInterface;
import managers.Stops;

import managers.StopsInterface;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class StopsAndStopProxyTest {

    private FactoryInterface factory;
    private Stops stops;
    private StopProxy stopProxy;

    private final StopName stopName = new StopName("Stop A");
    private final List<LineName> lines = List.of(new LineName("L1"), new LineName("L2"), new LineName("L3"));
    private Pair<Time, Optional<LineName>> stopData;

    @Before
    public void setUp() {
        factory = new FactoryInterface() {
            @Override
            public void setStops(StopsInterface stops) {

            }

            @Override
            public Optional<StopInterface> createStop(StopName stop) {
                if (!stopName.equals(stop)) return Optional.empty();
                return Optional.of(new StopInterface() {
                    @Override
                    public void updateReachableAt(Time time, LineName line) {
                        stopData = new Pair<>(time, Optional.ofNullable(line));
                    }

                    @Override
                    public Pair<Time, Optional<LineName>> getReachableAt() {
                        return new Pair<>(stopData.getFirst(), stopData.getSecond());
                    }

                    @Override
                    public StopName getStopName() {
                        return stopName;
                    }

                    @Override
                    public List<LineName> getLines() {
                        return lines;
                    }
                });
            }

            @Override
            public Optional<LineInterface> createLine(LineName lineName, Time time) {
                return Optional.empty();
            }

            @Override
            public void updateDatabase(List<LineSegmentInterface> lineSegments) {

            }
        };
        stops = new Stops(factory);
        stopProxy = new StopProxy(stops, stopName);
    }

    @Test
    public void getMethodsTest() throws SQLException, IncorrectUserInputException {
        StopName proxyStop = stopProxy.getStopName();
        assertEquals(proxyStop, stopName);

        assertFalse(stops.isLoaded(stopName));
        assertThrows(IllegalStateException.class, () -> stopProxy.getReachableAt());

        List<LineName> proxyLines = stopProxy.getLines();
        assertEquals(proxyLines, lines);
    }

    @Test
    public void updateReachableTest() throws SQLException, IncorrectUserInputException {
        stopProxy.updateReachableAt(new Time(10), new LineName("L5"));
        assertTrue(stops.isLoaded(stopName));
        Pair<Time, Optional<LineName>> data = stopProxy.getReachableAt();
        assertEquals(stopData.getFirst(), data.getFirst());
        assertTrue(stopData.getSecond().isPresent() && data.getSecond().isPresent());
        assertEquals(stopData.getSecond().get(), data.getSecond().get());
    }
}
