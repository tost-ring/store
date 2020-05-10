package app.core.suite;

import java.util.Iterator;
import java.util.Objects;

public final class Suite {

    public static class Add {}

    public static final Object OK = new Object();

    public static Subject set() {
        return new WrapSubject();
    }
    public static Subject set(Object element) {
        return new WrapSubject(new BubbleSubject(element));
    }
    public static Subject set(Object key, Object value) {
        return new WrapSubject(new CoupleSubject(key, value));
    }
    public static Add add() {
        return new Add();
    }
    public static Subject add(Object element) {
        return new WrapSubject(new CoupleSubject(new Add(), element));
    }
    public static Subject insetAll(Iterable<Subject> source) {
        return new WrapSubject(ZeroSubject.getInstance().insetAll(source));
    }
    public static Subject setAll(Iterable<Object> source) {
        return new WrapSubject(ZeroSubject.getInstance().setAll(source));
    }
    public static Subject addAll(Iterable<Object> source) {
        return new WrapSubject(ZeroSubject.getInstance().addAll(source));
    }

    public static Subject ok() {
        return new WrapSubject(new BubbleSubject(OK));
    }

    public static boolean isOk(Subject subject) {
        return subject.get(OK).settled();
    }

    public static Subject error(Object cause) {
        return new WrapSubject(new CoupleSubject("error", cause));
    }

    public static Subject fuse(Subject subject) {
        if(subject == null) {
            return Suite.set();
        } else if(subject.fused()) {
            return subject;
        } else {
            return new WrapSubject(new FuseSubject(subject));
        }
    }

    public static Subject thready() {
        return new ThreadySubject();
    }

    public static int hashCode(Subject subject) {
        int hash = 0xABCDEF;
        for(Subject it : subject.front()) {
            hash ^= Objects.hashCode(it.key()) ^ Objects.hashCode(it.direct());
        }
        return hash;
    }

    public static boolean equals(Subject subject1, Subject subject2) {
        if(subject1.size() != subject2.size()) return false;
        Iterator<Subject> it1 = subject1.front().iterator();
        Iterator<Subject> it2 = subject2.front().iterator();
        while(it1.hasNext() && it2.hasNext()) {
            Subject s1 = it1.next();
            Subject s2 = it2.next();
            if(!Objects.equals(s1.key(), s2.key()) || !Objects.equals(s1.direct(), s2.direct())) {
                return false;
            }
        }
        return true;
    }
}
