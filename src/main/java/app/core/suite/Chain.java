package app.core.suite;

import app.core.fluid.FluidIterator;
import app.core.fluid.FluidSubject;

import java.util.*;

public class Chain implements FluidSubject {

    static class Link {
        Link front;
        Link back;
        Subject subject;

        Link(Link front, Link back, Subject subject) {
            this.subject = subject;
            this.front = front;
            this.back = back;
        }

        Link(Link front, Link back, Object key, Object value) {
            subject = new CoupleSubject(key, value);
            this.front = front;
            this.back = back;
        }

        void setValue(Object value) {
            subject.set(subject.key().direct(), value);
        }
    }

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
            return current.front != ward;
        }

        public Subject next() {
            current = reverse ? current.back : current.front;
            return current.subject;
        }
    }

    private final Map<Object, Link> data = new HashMap<>();
    private final Link ward;

    public Chain() {
        this.ward = new Link(null, null, ZeroSubject.getInstance());
        this.ward.front = this.ward.back = ward;
    }

    public Subject get(Object key) {
        return data.getOrDefault(key, ward).subject;
    }

    public Subject getFirst() {
        return ward.front.subject;
    }

    public Subject getLast() {
        return ward.back.subject;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.size() == 0;
    }

    public Object put(Object key, Object value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);
        Object seniorValue = null;

        if(linkSenior == null) {

            ward.front.back = linkJunior;
            linkJunior.front = ward.front;
            linkJunior.back = ward;
            ward.front = linkJunior;

        } else {

            seniorValue = linkSenior.subject.direct();
            linkSenior.setValue(value);
            if(linkSenior != ward.front) {

                linkSenior.front.back = linkSenior.back;
                linkSenior.back.front = linkSenior.front;
                ward.front.back = linkSenior;
                linkSenior.front = ward.front;
                linkSenior.back = ward;
                ward.front = linkSenior;

            }

        }

        return seniorValue;
    }

    public void remove(Object key) {

        Link linkSenior = data.remove(key);

        if(linkSenior != null) {

            linkSenior.front.back = linkSenior.back;
            linkSenior.back.front = linkSenior.front;

        }

    }

    public void putIfAbsent(Object key, Object value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);

        if(linkSenior == null) {

            ward.front.back = linkJunior;
            linkJunior.front = ward.front;
            ward.front = linkJunior;
            linkJunior.back = ward;

        }

    }

    public void remove(Object key, Object value) {

        Link link = data.get(key);
        if (link != null && link.equals(value)) {
            data.remove(key);
        }
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
