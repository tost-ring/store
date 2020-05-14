package app.core.suite.util;

public class Interable {

    public static Fluid<Integer> indexes() {
        return () -> new FluidIterator<>() {
            int current = 0;

            @Override
            public boolean hasNext() {
                return current < Integer.MAX_VALUE;
            }

            @Override
            public Integer next() {
                return current++;
            }
        };
    }
}
