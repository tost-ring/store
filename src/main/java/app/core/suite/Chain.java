package app.core.suite;

import app.core.suite.util.FluidIterator;
import app.core.suite.util.FluidSubject;

import java.util.*;

class Chain implements FluidSubject {

    class ChainIterator implements FluidIterator<Subject> {

        private final boolean reverse;
        private Link current = ward;

        public ChainIterator() {
            this(false);
        }

        public ChainIterator(boolean reverse) {
            this.reverse = reverse;
        }

        public boolean hasNext() {
            return (reverse ? current.front() : current.back()) != ward;
        }

        public Subject next() {
            current = reverse ? current.front() : current.back();
            return current.subject;
        }
    }

    private final Map<Object, Link> data = new HashMap<>();
    final Link ward;

    public Chain() {
        this.ward = new Link(null, null, ZeroSubject.getInstance());
        this.ward.front = this.ward.back = ward;
    }

    Link get(Object key) {
        return data.getOrDefault(key, ward);
    }

    Link getFirst() {
        return ward.back;
    }

    Link getLast() {
        return ward.front;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.size() == 0;
    }

    Object put(Link before, Object key, Object value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);
        Object seniorValue = null;

        if(linkSenior == null) {

            before.front.back = linkJunior;
            linkJunior.front = before.front;
            linkJunior.back = before;
            before.front = linkJunior;

        } else {

            seniorValue = linkSenior.subject.direct();
            linkSenior.setValue(value);
            if(linkSenior != before.front) {

                linkSenior.front.back = linkSenior.back;
                linkSenior.back.front = linkSenior.front;
                before.front.back = linkSenior;
                linkSenior.front = before.front;
                linkSenior.back = before;
                before.front = linkSenior;

            }

        }

        return seniorValue;
    }

    void putIfAbsent(Link before, Object key, Object value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);

        if(linkSenior == null) {

            before.front.back = linkJunior;
            linkJunior.front = before.front;
            linkJunior.back = before;
            before.front = linkJunior;

        }

    }

    public Object putLast(Object key, Object value) {
        return put(ward, key, value);
    }

    public void putLastIfAbsent(Object key, Object value) {
        putIfAbsent(ward, key, value);
    }

    public Object putFirst(Object key, Object value) {
        return put(ward.back, key, value);
    }

    public void putFirstIfAbsent(Object key, Object value) {
        putIfAbsent(ward.back, key, value);
    }

    public void remove(Object key) {

        Link linkSenior = data.remove(key);

        if(linkSenior != null) {

            linkSenior.front.back = linkSenior.back;
            linkSenior.back.front = linkSenior.front;
            linkSenior.subject = null;
        }

    }

    public void remove(Object key, Object value) {

        Link link = data.get(key);
        if (link != null && link.equals(value)) {
            remove(key);
        }
    }

    public void clear() {
        for(Link link : data.values()) {
            link.front = link.back = ward;
        }
        data.clear();
    }

    @Override
    public FluidIterator<Subject> iterator() {
        return new ChainIterator();
    }

    public FluidIterator<Subject> iterator(boolean reverse) {
        if(reverse) {
            return new ChainIterator( true);
        } else {
            return new ChainIterator();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        forEach(s -> stringBuilder.append(s.toString()).append('\n'));
        return stringBuilder.toString();
    }
}
