package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;
import app.core.suite.action.*;

import java.util.Iterator;
import java.util.Objects;

public final class Suite {

    public static Subject set() {
        return new WrapSubject();
    }
    public static Subject set(Object value) {
        return new WrapSubject(new PrimeSubject(value));
    }
    public static Subject set(Object key, Object value) {
        return new WrapSubject(new PrimeSubject(key, value));
    }

    public static Subject met(Subject source) {
        return new WrapSubject(ZeroSubject.getInstance().met(source));
    }
    public static Subject met(Subject source, Object... keys) {
        return new WrapSubject(ZeroSubject.getInstance().met(source, keys));
    }
    public static Subject mix(Subject source, Object... keys) {
        return new WrapSubject(ZeroSubject.getInstance().mix(source, keys));
    }

    public static Subject ok() {
        return new WrapSubject(new PrimeSubject("ok"));
    }

    public static Subject error(Object cause) {
        return new WrapSubject(new PrimeSubject("error", cause));
    }

    public static <C> FlowCollection<C> values(Subject subject) {
        FlowArrayList<C> flowArrayList = new FlowArrayList<>();
        for(Subject it : subject) {
            flowArrayList.add(it.get());
        }
        return flowArrayList;
    }

    public static <C> FlowCollection<C> values(Subject subject, Glass<C, C> glassFilter) {
        FlowArrayList<C> flowArrayList = new FlowArrayList<>();
        for(Subject it : subject) {
            if(it.is(glassFilter)) {
                flowArrayList.add(it.get());
            }
        }
        return flowArrayList;
    }

    public static <C> FlowCollection<C> values(Subject subject, Class<C> classFilter) {
        FlowArrayList<C> flowArrayList = new FlowArrayList<>();
        for(Subject it : subject) {
            if(it.isi(classFilter)) {
                flowArrayList.add(it.get());
            }
        }
        return flowArrayList;
    }

    public static <C> FlowCollection<C> keys(Subject subject) {
        FlowArrayList<C> flowArrayList = new FlowArrayList<>();
        for(Subject it : subject) {
            flowArrayList.add(it.getKey());
        }
        return flowArrayList;
    }

    public static <C> FlowCollection<C> keys(Subject subject, Glass<C, C> glassFilter) {
        FlowArrayList<C> flowArrayList = new FlowArrayList<>();
        for(Subject it : subject) {
            C key = it.godKey(null, glassFilter);
            if(key != null) {
                flowArrayList.add(key);
            }
        }
        return flowArrayList;
    }

    public static <C> FlowCollection<C> keys(Subject subject, Class<C> classFilter) {
        FlowArrayList<C> flowArrayList = new FlowArrayList<>();
        for(Subject it : subject) {
            C key = it.godKey(null, classFilter);
            if(key != null) {
                flowArrayList.add(key);
            }
        }
        return flowArrayList;
    }

    public static int hashCode(Subject subject) {
        int hash = 0xABCDEF;
        for(Subject it : subject) {
            hash^= Objects.hashCode(it.getKey()) ^ Objects.hashCode(it.get());
        }
        return hash;
    }

    public static boolean equals(Subject subject1, Subject subject2) {
        if(subject1.size() != subject2.size()) return false;
        Iterator<Subject> it1 = subject1.iterator();
        Iterator<Subject> it2 = subject2.iterator();
        while(it1.hasNext() && it2.hasNext()) {
            Subject s1 = it1.next();
            Subject s2 = it2.next();
            if(!Objects.equals(s1.getKey(), s2.getKey()) || !Objects.equals(s1.get(), s2.get())) {
                return false;
            }
        }
        return true;
    }

    public static Subject setFun(Object key, Function function) {
        return new WrapSubject(new ChainSubject().setFun(key, function));
    }
    public static Subject setFun(Object key, Impression impression) {
        return new WrapSubject(new ChainSubject().setFun(key, impression));
    }
    public static Subject setFun(Object key, Expression expression) {
        return new WrapSubject(new ChainSubject().setFun(key, expression));
    }
    public static Subject setFun(Object key, Statement statement) {
        return new WrapSubject(new ChainSubject().setFun(key, statement));
    }
    public static Subject setHun(Object key, HazardousFunction function) {
        return new WrapSubject(new ChainSubject().setFun(key, function));
    }
    public static Subject setHun(Object key, HazardousImpression impression) {
        return new WrapSubject(new ChainSubject().setFun(key, impression));
    }
    public static Subject setHun(Object key, HazardousExpression expression) {
        return new WrapSubject(new ChainSubject().setFun(key, expression));
    }
}
