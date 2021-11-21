package factories;

import components.LineInterface;
import dataTypes.LineName;

import java.util.Optional;

public interface LineFactoryInterface {

    Optional<LineInterface> createLine(LineName lineName);
}
