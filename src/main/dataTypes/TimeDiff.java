package dataTypes;

import java.util.Objects;

public class TimeDiff {

    private final long time;

    public TimeDiff(long time) {
        if (time < 0) throw new IllegalArgumentException("Time value cannot be negative.");
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeDiff timeDiff = (TimeDiff) o;
        return time == timeDiff.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    public long getTime() {
        return time;
    }
}
