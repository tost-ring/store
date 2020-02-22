package app.core.flow;

import app.core.suite.Prospect;
import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FlowCollection<E> extends Collection<E>, Subjective {


    FlowArrayList<E> asFAL();
    FlowHashSet<E> asFHS();
    FlowCollection<E> getBy(Predicate<E> predicate);
    <M> FlowCollection<M> mapTo(Function<E,M> function);
    <M> FlowCollection<M> mapTo(Function<E, M> function, boolean skipNulls);

    default FlowCollection<E> setAll(Collection<E> collection) {
        clear();
        addAll(collection);
        return this;
    }

    default E find(Predicate<E> predicate){
        for (E it : this){
            if(predicate.test(it))return it;
        }
        return null;
    }

    default E find(){
        return find(e->true);
    }

    default<C extends Collection<E>> C getBy(Predicate<E> predicate, C collection){
        stream().filter(predicate).forEach(collection::add);
        return collection;
    }

    default<M, C extends Collection<M>> C mapTo(Function<E,M> function, C collection){
        stream().map(function).forEach(collection::add);
        return collection;
    }

    default boolean contains(Predicate<E> predicate) {
        return find(predicate) != null;
    }

    default FlowArrayList<E> sortedBy(Comparator<E> comparator) {
        FlowArrayList<E> flowArrayList = asFAL();
        flowArrayList.sort(comparator);
        return flowArrayList;
    }

    @Override
    default Subject toSubject() {
        return Prospect.collectionSubjectively(Suite.set(this));
    }

    @Override
    default void fromSubject(Subject subject) {
        Prospect.collectionObjectively(Suite.set(Object.class, this).set(Subject.class, subject));
    }
}
