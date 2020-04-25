package app.core.jorg;

import app.core.suite.Graph;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.Objects;

public class Xkey {
    private Object object;
    private Object label;
    private Subject pre;
    private Subject post;

    public Xkey(Object object, Object label) {
        this.object = object;
        this.label = label;
        this.pre = Suite.set();
        this.post = Suite.set();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getLabel() {
        return label;
    }

    public void setLabel(Object label) {
        this.label = label;
    }

    public Subject getPre() {
        return pre;
    }

    public Subject getPost() {
        return post;
    }

    public void addPre(Xkey xkey) {
        pre.add(xkey);
    }

    public void setPost(Xkey key, Xkey value) {
        post.set(key, value);
    }

    public void addPost(Xkey xkey) {
        post.add(xkey);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Xkey && Objects.equals(object, ((Xkey) o).object) && Objects.equals(label, ((Xkey) o).label);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(object) ^ Objects.hashCode(label);
    }

    @Override
    public String toString() {
        return label != null ? label.toString() : object.toString();
    }

    public String dataAsString() {
        return " [ " + label + " ] " + pre.front().values().toString(" ] ") + "\n" + post.toString();
    }
}
