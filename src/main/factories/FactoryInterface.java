package factories;

import components.*;
import dataTypes.*;

import java.util.Optional;

public interface FactoryInterface {

    Optional<StopInterface> createStop(StopName stopName);

    Optional<LineInterface> createLine(LineName lineName);

}
