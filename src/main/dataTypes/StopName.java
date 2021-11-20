package dataTypes;

import java.util.Objects;

public class StopName {

    private final String name;

    public StopName(String name) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null.");
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopName stopName = (StopName) o;
        return name.equals(stopName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
