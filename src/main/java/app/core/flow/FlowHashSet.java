package app.core.flow;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlowHashSet<E> extends HashSet<E> implements FlowCollection<E>{

    public FlowHashSet() {}

    public FlowHashSet(Collection<? extends E> c) {
        super(c);
    }

    @SafeVarargs
    public FlowHashSet(E ... es) {
        super(Arrays.asList(es));
    }

    public FlowHashSet(Iterable<E> iterable) {
        iterable.forEach(this::add);
    }

    @Override
    public FlowArrayList<E> asFAL() {
        return new FlowArrayList<>((Iterable<E>)this);
    }

    @Override
    public FlowHashSet<E> asFHS() {
        return this;
    }

    @Override
    public FlowHashSet<E> getBy(Predicate<E> predicate){
        return stream().filter(predicate).collect(Collectors.toCollection(FlowHashSet::new));
    }


    @Override
    public<M> FlowHashSet<M> mapTo(Function<E,M> function){
        return stream().map(function).collect(Collectors.toCollection(FlowHashSet::new));
    }

    @Override
    public<M> FlowHashSet<M> mapTo(Function<E, M> function, boolean skipNulls) {
        return skipNulls ?
                stream().map(function).filter(Objects::nonNull).collect(Collectors.toCollection(FlowHashSet::new))
                : mapTo(function);
    }
}
