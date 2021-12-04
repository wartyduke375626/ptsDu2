package dataTypes;

import java.util.Objects;

public class Time implements Comparable<Time>{

    private final long time;

    public Time(long time) {
        if (time < 0) throw new IllegalArgumentException("Time value cannot be negative.");
        this.time = time;
    }

    public Time(Time time) {
        this.time = time.time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time1 = (Time) o;
        return time == time1.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return Long.toString(time);
    }

    @Override
    public int compareTo(Time o) {
        return Long.compareUnsigned(this.time, o.time);
    }
}
