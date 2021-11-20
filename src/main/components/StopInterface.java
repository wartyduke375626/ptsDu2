package components;

import dataTypes.*;

import java.util.List;

public interface StopInterface {

    void updateReachableAt(Time time, LineName line);

    void updateReachableAt(Time time);

    Pair<Time, LineName> getReachableAt();

    List<LineName> getLines();
}
