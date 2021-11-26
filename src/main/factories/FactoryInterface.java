package factories;

import components.*;
import dataTypes.*;
import managers.StopsInterface;

import java.util.Optional;

public interface FactoryInterface {

    Optional<StopInterface> createStop(StopName stopName);

    Optional<LineInterface> createLine(LineName lineName, Time time);

}
