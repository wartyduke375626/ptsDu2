package managersTests;

import components.LineInterface;
import components.StopInterface;
import dataTypes.*;

import factories.FactoryInterface;
import managers.Stops;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class StopsTest {

    private FactoryInterface factory;
    private Stops stops;
    private final Map<StopName, Time> factoryStops = Map.of(
            new StopName("Stop A"), new Time(10),
            new StopName("Stop B"), new Time(20),
            new StopName("Stop C"), new Time(30)
    );

    @Before
    public void setUp() {
        factory = new FactoryInterface() {
            @Override
            public Optional<StopInterface> createStop(StopName stopName) {
                if (!factoryStops.containsKey(stopName)) return Optional.empty();
                return Optional.of(new StopInterface() {
                    @Override
                    public void updateReachableAt(Time time, LineName line) {

                    }

                    @Override
                    public Pair<Time, Optional<LineName>> getReachableAt() {
                        return new Pair<>(factoryStops.get(stopName), Optional.empty());
                    }

                    @Override
                    public StopName getStopName() {
                        return stopName;
                    }

                    @Override
                    public List<LineName> getLines() {
                        return null;
                    }
                });
            }

            @Override
            public Optional<LineInterface> createLine(LineName lineName, Time time) {
                return Optional.empty();
            }
        };
        stops = new Stops(factory);
    }

    @Test
    public void earliestReachableStopAfterTest() {
        assertTrue(stops.earliestReachableStopAfter(new Time(10)).isEmpty());
        stops.loadStop(new StopName("Stop A"));
        stops.loadStop(new StopName("Stop B"));

        Optional<Pair<StopName, Time>> tmp = stops.earliestReachableStopAfter(new Time(5));
        assertTrue(tmp.isPresent());
        Pair<StopName, Time> data = tmp.get();
        assertEquals(data.getFirst(), new StopName("Stop A"));
        assertEquals(data.getSecond(), new Time(10));

        tmp = stops.earliestReachableStopAfter(new Time(10));
        assertTrue(tmp.isPresent());
        data = tmp.get();
        assertEquals(data.getFirst(), new StopName("Stop B"));
        assertEquals(data.getSecond(), new Time(20));

        assertTrue(stops.earliestReachableStopAfter(new Time(22)).isEmpty());
        stops.loadStop(new StopName("Stop C"));

        tmp = stops.earliestReachableStopAfter(new Time(22));
        assertTrue(tmp.isPresent());
        data = tmp.get();
        assertEquals(data.getFirst(), new StopName("Stop C"));
        assertEquals(data.getSecond(), new Time(30));

        assertThrows(NoSuchElementException.class, () -> stops.loadStop(new StopName("Stop D")));
        assertThrows(NoSuchElementException.class, () -> stops.getStop(new StopName("Stop D")));
    }
}
