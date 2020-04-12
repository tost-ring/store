package app.core.flow;

public class Interable {

    public static FlowIterable<Integer> indexes() {
        return () -> new FlowIterator<>() {
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
