package app.core.suite;

import java.util.Collections;
import java.util.Iterator;

public class Graph {

    private static Subject fakeSub = ZeroSubject.getInstance();
    private Subject sub = Suite.set();

    public void putNode(Object node) {
        sub.sos(node);
    }

    public void removeNode(Object node) {
        sub.unset(node);
        sub.forEach(s -> s.getAs(Subject.class).unset(node));
    }

    public Iterable<Subject> getNodes() {
        return () -> new Iterator<>() {
            Iterator<Subject> origin = sub.iterator();

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public Subject next() {
                return Suite.set(origin.next().getKey());
            }
        };
    }

    public Iterable<Subject> getNodes(Object node) {
        return () -> new Iterator<>() {
            Iterator<Subject> origin = sub.god(node, fakeSub).iterator();

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public Subject next() {
                return Suite.set(origin.next().getKey());
            }
        };
    }

    public Subject getNode() {
        return sub.iterator().nod(null);
    }

    public Subject getNode(Object node) {
        return sub.god(node, fakeSub).iterator().nod(null);
    }

    public int countNodes() {
        return sub.size();
    }

    public int countNodes(Object node) {
        return sub.god(node, fakeSub).size();
    }

    public boolean containsNode(Object node) {
        return sub.is(node);
    }

    public Iterable<Subject> getLinks() {
        return () -> new Iterator<>() {
            Iterator<Subject> lowerIterator = getNodes().iterator();
            Iterator<Subject> higherIterator = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                if(higherIterator.hasNext()) return true;
                while(lowerIterator.hasNext()) {
                    higherIterator = getLinks(lowerIterator.next()).iterator();
                    if(higherIterator.hasNext()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Subject next() {
                return higherIterator.next();
            }
        };
    }

    public Iterable<Subject> getLinks(Object node) {
        return () -> new Iterator<>() {
            Iterator<Subject> lowerIterator = sub.god(node, fakeSub).iterator();
            Iterator<Subject> higherIterator = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                if(higherIterator.hasNext()) return true;
                while(lowerIterator.hasNext()) {
                    higherIterator = lowerIterator.next().getAs(Subject.class).iterator();
                    if(higherIterator.hasNext()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Subject next() {
                return Suite.set(higherIterator.next().getKey());
            }
        };
    }

    public Subject getLinks(Object from, Object to) {
        return sub.god(from, fakeSub).gom(to, Suite::set).fuse();
    }

    public Iterator<Subject> iterator() {
        return new Iterator<>() {
            Iterator<Subject> fromIterator = sub.iterator();
            Iterator<Subject> viaToIterator = Collections.emptyIterator();
            Object from = null;

            @Override
            public boolean hasNext() {
                if (viaToIterator.hasNext()) return true;
                if (fromIterator.hasNext()) {
                    Subject next = fromIterator.next();
                    from = next.getKey();
                    viaToIterator = from(from).iterator();
                    return true;
                }
                return false;
            }

            @Override
            public Subject next() {
                return viaToIterator.next().set("from", from);
            }
        };
    }

    public Iterable<Subject> from(Object from) {
        return () -> new Iterator<>() {
            Iterator<Subject> toIterator = sub.god(from, fakeSub).iterator();
            Iterator<Subject> viaIterator = Collections.emptyIterator();
            Object to = null;

            @Override
            public boolean hasNext() {
                if (viaIterator.hasNext()) return true;
                if (toIterator.hasNext()) {
                    Subject next = toIterator.next();
                    to = next.getKey();
                    viaIterator = getLinks(from, to).iterator();
                    return true;
                }
                return false;
            }

            @Override
            public Subject next() {
                return Suite.set("to", to).set("via", viaIterator.next().getKey());
            }
        };
    }

    public void connect(Object from, Object to) {
        sub.gs(from, Suite.set()).gs(to, Suite.set());
    }

    public void connect(Object from, Object to, Object via) {
        sub.gs(from, Suite.set()).gs(to, Suite.set()).set(via);
    }

    public void disconnect(Object from, Object to) {
        sub.gom(from, Suite::set).unset(to);
    }

    public void disconnect(Object from, Object to, Object via) {
        sub.gom(from, Suite::set).gom(to, Suite::set).unset(via);
    }

    public boolean connected(Object from, Object to) {
        return sub.gom(from, Suite::set).is(to);
    }

    public boolean connected(Object from, Object to, Object via) {
        return sub.gom(from, Suite::set).gom(to, Suite::set).is(via);
    }

    public void merge(Graph that) {
        sub.met(that.sub);
    }

    @Override
    public String toString() {
        return sub.toString();
    }
}
