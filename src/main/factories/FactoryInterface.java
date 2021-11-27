package factories;

import components.*;
import dataTypes.*;
import managers.StopsInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FactoryInterface {

    void setStops(StopsInterface stops);

    Optional<StopInterface> createStop(StopName stopName) throws SQLException;

    Optional<LineInterface> createLine(LineName lineName, Time time) throws SQLException;

    void updateDatabase(List<LineSegmentInterface> lineSegments) throws SQLException;

}
