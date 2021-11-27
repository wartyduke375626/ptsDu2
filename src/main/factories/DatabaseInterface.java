package factories;

import dataTypes.LineName;
import dataTypes.StopName;

import java.util.List;
import java.util.Optional;

public interface DatabaseInterface {

    Optional<List<LineName>> getLinesAtStop(StopName stopName);
}
