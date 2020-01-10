package app.core.flow;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlowArrayList<E> extends ArrayList<E> implements FlowCollection<E> {

    @SafeVarargs
    public static<M> FlowArrayList<M> melt(M ... es) {
        return new FlowArrayList<>(es);
    }

    public FlowArrayList() {}

    public FlowArrayList(Collection<? extends E> c) {
        super(c);
    }

    @SafeVarargs
    public FlowArrayList(E ... es) {
        this(Arrays.asList(es));
    }

    @Override
    public FlowArrayList<E> asFAL() {
        return this;
    }

    @Override
    public FlowHashSet<E> asFHS() {
        return new FlowHashSet<>(this);
    }

    @Override
    public FlowCollection<E> getBy(Predicate<E> predicate) {
        return stream().filter(predicate).collect(Collectors.toCollection(FlowArrayList::new));
    }

    @Override
    public <M> FlowCollection<M> mapTo(Function<E, M> function) {
        return stream().map(function).collect(Collectors.toCollection(FlowArrayList::new));
    }

    @Override
    public <M> FlowCollection<M> mapTo(Function<E, M> function, boolean skipNulls) {
        return skipNulls ?
                stream().map(function).filter(Objects::nonNull).collect(Collectors.toCollection(FlowArrayList::new))
                : mapTo(function);
    }
}
