package components;

import dataTypes.*;

import java.util.List;

public interface StopInterface {

    void updateReachableAt(Time time, LineName line);

    Pair<Time, LineName> getReachableAt();

    StopName getStopName();

    List<LineName> getLines();
}
