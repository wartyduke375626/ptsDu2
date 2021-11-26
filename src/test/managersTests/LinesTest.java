package managersTests;

import components.LineInterface;
import components.StopInterface;
import dataTypes.*;

import factories.FactoryInterface;
import managers.Lines;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class LinesTest {

    private FactoryInterface factory;
    private Lines lines;
    private final LineName lineName = new LineName("L1");
    private final TimeDiff timeDiff = new TimeDiff(10);
    private final List<Pair<StopName, Time>> lineStops = new ArrayList<>(List.of(
            new Pair<>(new StopName("Stop A"), new Time(Long.MAX_VALUE)),
            new Pair<>(new StopName("Stop B"), new Time(Long.MAX_VALUE)),
            new Pair<>(new StopName("Stop C"), new Time(Long.MAX_VALUE))
    ));

    @Before
    public void setUp() {
        factory = new FactoryInterface() {
            @Override
            public Optional<StopInterface> createStop(StopName stopName) {
                return Optional.empty();
            }

            @Override
            public Optional<LineInterface> createLine(LineName line, Time time) {
                if (!lineName.equals(line)) return Optional.empty();
                return Optional.of(new LineInterface() {
                    @Override
                    public void updateReachable(Time time, StopName stop) {
                        for (int i=0; i<lineStops.size(); i++) {
                            lineStops.set(i, new Pair<>(
                                    lineStops.get(i).getFirst(),
                                    new Time(i*timeDiff.getTime())
                            ));
                        }
                    }

                    @Override
                    public StopName updateCapacityAndGetPreviousStop(StopName stop, Time time) {
                        for (int i=0; i<lineStops.size(); i++) {
                            Pair<StopName, Time> data = lineStops.get(i);
                            if (data.getFirst().equals(stop)) return lineStops.get(i-1).getFirst();
                        }
                        return null;
                    }
                });
            }
        };
        lines = new Lines(factory);
    }

    @Test
    public void updateReachableTest() {
        lines.updateReachable(List.of(new LineName("L1")), new StopName("Stop A"), new Time(0));
        for (int i=0; i<lineStops.size(); i++) {
            assertEquals(lineStops.get(i).getSecond(), new Time(i*timeDiff.getTime()));
        }

        assertThrows(NoSuchElementException.class, () -> lines.updateReachable(List.of(new LineName("L5")), new StopName("Stop A"), new Time(0)));
    }

    @Test
    public void updateCapacityAndGetPreviousStopTest() {
        assertThrows(NoSuchElementException.class, () -> lines.updateCapacityAndGetPreviousStop(lineName, new StopName("Stop B"), new Time(10)));
        lines.updateReachable(List.of(new LineName("L1")), new StopName("Stop A"), new Time(0));

        StopName stopName = lines.updateCapacityAndGetPreviousStop(lineName, new StopName("Stop B"), new Time(10));
        assertEquals(stopName, new StopName("Stop A"));

        assertThrows(IndexOutOfBoundsException.class, () -> lines.updateCapacityAndGetPreviousStop(lineName, new StopName("Stop A"), new Time(0)));
    }
}
