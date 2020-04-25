package app.core.suite;

import app.core.fluid.FluidIterator;
import app.core.fluid.FluidSubject;

import java.util.*;

public class Chain implements FluidSubject {

    public static class Link extends CoupleSubject {
        private Link front;
        private Link back;

        public Link(Link front, Link back, Object key, Object value) {
            super(key, value);
            this.front = front;
            this.back = back;
        }

        Link(Link that){
            super(that.key().direct(), that.direct());
            this.front = that.front;
            this.back = that.back;
        }

        public Link getFront() {
            return front;
        }

        public Link getBack() {
            return back;
        }

        void setValue(Object value) {
            set(key().direct(), value);
        }
    }

    public static class ChainIterator implements FluidIterator<Subject> {

        private final boolean reverse;
        private final Link first;
        private final Link last;
        private Link current;

        public ChainIterator(Link first, Link last) {
            this(first, last, false);
        }

        public ChainIterator(Link first, Link last, boolean reverse) {
            this.first = first;
            this.last = last;
            this.reverse = reverse;
        }

        public boolean hasNext() {
            return current == null ? first != null : current != last;
        }

        public Link next() {
            current = current == null ? first : reverse ? current.back : current.front;
            return current;
        }
    }

    private final Map<Object, Link> data = new HashMap<>();
    private Link head;
    private final Link zeroLink = new Link(null, null, null, null);

    public void rotateFront() {
        if(head != null)
            head = head.front;
    }

    public void rotateBack() {
        if(head != null)
            head = head.back;
    }

    public boolean rotateAt(Object key) {
        Link link = data.get(key);
        if(link == null)return false;
        head = link;
        return true;
    }

    public Link getLink(Object key) {
        return data.get(key);
    }

    public Link getFirstLink() {
        return head;
    }

    public Link getLastLink() {
        return head == null ? null : head.back;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.size() == 0;
    }

    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return data.containsValue(new Link(null, null, null, value));
    }

    public Object get(Object key) {
        return data.getOrDefault(key, zeroLink).asExpected();
    }

    public Object put(Object key, Object value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);
        Object seniorValue = null;

        if(linkSenior == null) {
            if(head == null) {
                head = linkJunior.front = linkJunior.back = linkJunior;
            } else {
                linkJunior.front = head;
                linkJunior.back = head.back;
                head.back.front = linkJunior;
                head.back = linkJunior;
            }
        } else {
            seniorValue = linkSenior.asExpected();
            linkSenior.setValue(value);
            if(linkSenior != head) {
                if(linkSenior != head.back) {
                    linkSenior.front.back = linkSenior.back;
                    linkSenior.back.front = linkSenior.front;
                    linkSenior.front = head;
                    linkSenior.back = head.back;
                    head.back.front = linkSenior;
                    head.back = linkSenior;
                }
                head = linkSenior;
            }
            head = head.front;
        }

        return seniorValue;
    }

    public Object remove(Object key) {

        Link linkSenior = data.remove(key);
        Object seniorValue = null;

        if(linkSenior != null) {
            seniorValue = linkSenior.asExpected();
            if(linkSenior == head) {
                if(linkSenior == head.front) {
                    head = null;
                } else {
                    linkSenior.front.back = linkSenior.back;
                    linkSenior.back.front = linkSenior.front;
                    head = linkSenior.front;
                }
            } else {
                linkSenior.front.back = linkSenior.back;
                linkSenior.back.front = linkSenior.front;
            }
        }

        return seniorValue;
    }

    public Object putIfAbsent(Object key, Object value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);
        Object seniorValue = null;

        if(linkSenior == null) {
            if(head == null) {
                head = linkJunior.front = linkJunior.back = linkJunior;
            } else {
                linkJunior.front = head;
                linkJunior.back = head.back;
                head.back.front = linkJunior;
                head.back = linkJunior;
                head = linkJunior;
            }
        } else {
            seniorValue = linkSenior.asExpected();
        }

        return seniorValue;
    }

    public void remove(Object key, Object value) {

        Link link = data.get(key);
        if (link != null && link.equals(value)) {
            data.remove(key);
        }
    }

    @Override
    public FluidIterator<Subject> iterator() {
        return new ChainIterator(head, head == null ? null : head.back);
    }

    public FluidIterator<Subject> iterator(boolean reverse) {
        if(reverse) {
            return new ChainIterator(head == null ? null : head.back, head, true);
        } else {
            return new ChainIterator(head, head == null ? null : head.back);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        forEach(s -> stringBuilder.append(s.toString()).append('\n'));
        return stringBuilder.toString();
    }
}
