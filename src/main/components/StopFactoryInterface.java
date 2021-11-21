package components;

import dataTypes.StopName;

import java.util.Optional;

public interface StopFactoryInterface {

    Optional<StopInterface> createStop(StopName stopName);
}
