import java.util.Objects;

public class Time implements Comparable<Time>{

    private long time;

    public Time(long time) {
        if (time < 0) throw new IllegalArgumentException("Time value cannot be negative.");
        this.time = time;
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

    public void setTime(long time) {
        if (time < 0) throw new IllegalArgumentException("Time value cannot be negative.");
        this.time = time;
    }

    @Override
    public int compareTo(Time o) {
        return Long.compareUnsigned(this.time, o.time);
    }
}
