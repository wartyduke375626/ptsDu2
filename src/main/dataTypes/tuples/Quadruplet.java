package dataTypes.tuples;

import java.util.Objects;

public class Quadruplet<T1, T2, T3, T4> {

    private final T1 first;
    private final T2 second;
    private final T3 third;
    private final T4 forth;

    public Quadruplet(T1 first, T2 second, T3 third, T4 forth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.forth = forth;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T3 getThird() {
        return third;
    }

    public T4 getForth() {
        return forth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruplet<?, ?, ?, ?> that = (Quadruplet<?, ?, ?, ?>) o;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second) && Objects.equals(third, that.third) && Objects.equals(forth, that.forth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, forth);
    }
}
