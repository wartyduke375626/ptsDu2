package factories;

import components.*;
import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;
import database.DatabaseInterface;
import managers.StopsInterface;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

public class DatabaseFactory implements FactoryInterface {

    private final DatabaseInterface database;
    private final TimeDiff maxStartTimeDifference;
    private StopsInterface stops = null;

    public DatabaseFactory(DatabaseInterface database, TimeDiff maxStartTimeDifference) {
        this.database = database;
        this.maxStartTimeDifference = maxStartTimeDifference;
    }

    @Override
    public void setStops(StopsInterface stops) {
        this.stops = stops;
    }

    @Override
    public Optional<StopInterface> createStop(StopName stopName) throws SQLException {
        if (stops == null) throw new IllegalStateException("Stops have not been initialized yet.");

        database.startSession();
        Optional<List<LineName>> stopData = database.getStopData(stopName);
        database.endSession();

        if (stopData.isEmpty()) return Optional.empty();

        return Optional.of(new Stop(stopName, stopData.get()));

    }

    @Override
    public Optional<LineInterface> createLine(LineName lineName, Time time) throws SQLException {
        if (stops == null) throw new IllegalStateException("Stops have not been initialized yet.");

        database.startSession();
        Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> lineData = database.getLineFirstStopAndLineSegmentsData(lineName);
        if (lineData.isEmpty()) {
            database.endSession();
            return Optional.empty();
        }

        Optional<Map<Time, Pair<Integer, List<Pair<Integer, Integer>>>>> busesData = database.getBussesAndPassengers(lineName, time, maxStartTimeDifference);
        database.endSession();

        if (busesData.isEmpty()) throw new SQLIntegrityConstraintViolationException("Line with no buses in database.");

        StopName firstStop = lineData.get().getFirst();
        List<Time> startingTimes = new ArrayList<>(busesData.get().keySet());
        Collections.sort(startingTimes);
        lineData.get().getSecond().sort(Comparator.comparing(Triplet::getFirst));

        int capacity = busesData.get().get(startingTimes.get(0)).getFirst();
        for (Time t : startingTimes) {
            if (busesData.get().get(t).getFirst() != capacity) throw  new SQLIntegrityConstraintViolationException("Line with different bus capacities in database");
            if (busesData.get().get(t).getSecond().size() != lineData.get().getSecond().size()) throw new SQLIntegrityConstraintViolationException("Bus segments do not match with line segments in database.");
            busesData.get().get(t).getSecond().sort(Comparator.comparing(Pair::getFirst));
        }

        List<LineSegmentInterface> lineSegments = new ArrayList<>();
        TimeDiff totalTimeDiff = new TimeDiff(0);
        for (int i=0; i<lineData.get().getSecond().size(); i++) {
            TimeDiff timeToNextStop = lineData.get().getSecond().get(i).getThird();
            StopInterface nextStop = new StopProxy(stops, lineData.get().getSecond().get(i).getSecond());

            Map<Time, Integer> numberOfPassengers = new HashMap<>();
            for (Time t : startingTimes) {
                Time segmentStartTime = new Time(t.getTime() + totalTimeDiff.getTime());
                int passengerCount = busesData.get().get(t).getSecond().get(i).getSecond();
                if (passengerCount > capacity) throw new SQLIntegrityConstraintViolationException("BusSegment has more passengers than capacity in database.");
                numberOfPassengers.put(segmentStartTime, passengerCount);
            }

            lineSegments.add(new LineSegment(timeToNextStop, totalTimeDiff, nextStop, capacity, lineName, numberOfPassengers, i));
            totalTimeDiff = new TimeDiff(totalTimeDiff.getTime() + timeToNextStop.getTime());
        }

        return Optional.of(new Line(lineName, startingTimes, firstStop, lineSegments));
    }

    @Override
    public void updateDatabase(List<LineSegmentInterface> lineSegments) throws SQLException {
        Map<Pair<LineName, Time>, List<Pair<Integer, Integer>>> busesAndSegmentIndexesToUpdate = new HashMap<>();
        for (LineSegmentInterface ls : lineSegments) {
            LineName lineName = ls.getLine();
            Map<Time, Integer> updatedBuses = ls.getUpdatedBusses();

            for (Time startTime : updatedBuses.keySet()) {
                Pair<LineName, Time> key = new Pair<>(lineName, new Time(startTime.getTime() - ls.getTimeDiffFromStart().getTime()));
                if (!busesAndSegmentIndexesToUpdate.containsKey(key)) busesAndSegmentIndexesToUpdate.put(key, new ArrayList<>());
                busesAndSegmentIndexesToUpdate.get(key).add(new Pair<>(ls.getSegmentIndex(), updatedBuses.get(startTime)));
            }
        }

        database.startSession();
        database.updateBusPassengers(busesAndSegmentIndexesToUpdate);
        database.endSession();
    }
}
