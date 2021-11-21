package factories;

import components.Line;
import components.LineInterface;
import components.LineSegmentInterface;
import dataTypes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryLineFactory implements LineFactoryInterface {

    private final Map<LineName, Triplet<List<Time>, StopName, List<LineSegmentInterface>>> inMemoryLines;

    public InMemoryLineFactory(Map<LineName, Triplet<List<Time>, StopName, List<LineSegmentInterface>>> inMemoryLines) {
        this.inMemoryLines = new HashMap<>(inMemoryLines);
    }

    @Override
    public Optional<LineInterface> createLine(LineName lineName) {
        if (!inMemoryLines.containsKey(lineName)) return Optional.empty();
        else {
            Triplet<List<Time>, StopName, List<LineSegmentInterface>> data = inMemoryLines.get(lineName);
            return Optional.of(new Line(lineName, data.getFirst(), data.getSecond(), data.getThird()));
        }
    }
}
