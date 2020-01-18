package app.core.flow;

import app.core.suite.Prospect;
import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlowArrayList<E> extends ArrayList<E> implements FlowCollection<E>, Subjective {

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

    @Override
    public Subject toSubject() {
        return Prospect.collectionSubjectively(Suite.set(this));
    }

    @Override
    public Subject fromSubject(Subject subject) {
        return Prospect.collectionObjectively(Suite.set(Object.class, this).set(Subject.class, subject));
    }
}
