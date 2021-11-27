package factories;

import components.LineInterface;
import components.LineSegmentInterface;
import components.StopInterface;
import dataTypes.*;
import database.DatabaseInterface;
import managers.StopsInterface;

import java.util.List;
import java.util.Optional;

public class DatabaseFactory implements FactoryInterface {

    private final DatabaseInterface database;
    private StopsInterface stops = null;

    public DatabaseFactory(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public void setStops(StopsInterface stops) {
        this.stops = stops;
    }

    @Override
    public Optional<StopInterface> createStop(StopName stopName) {
        if (stops == null) throw new IllegalStateException("Stops have not been initialized yet.");
        return Optional.empty();
    }

    @Override
    public Optional<LineInterface> createLine(LineName lineName, Time time) {
        if (stops == null) throw new IllegalStateException("Stops have not been initialized yet.");

        return Optional.empty();
    }

    @Override
    public void updateDatabase(List<LineSegmentInterface> lineSegments) {

    }
}
