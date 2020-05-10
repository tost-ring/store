package app.core.jorg;

import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.Objects;

public class Xkey {
    private Object object;
    private Object label;
    private final Subject image;
    private boolean constructed;
    private boolean underConstruction;

    public Xkey(Object object, Object label, boolean constructed) {
        this.object = object;
        this.label = label;
        this.image = Suite.set();
        this.constructed = constructed;
        this.underConstruction = false;
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

    public Subject getImage() {
        return image;
    }

    public boolean isConstructed() {
        return constructed;
    }

    public void setConstructed(boolean constructed) {
        this.constructed = constructed;
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }

    public void set(Xkey key, Xkey value) {
        image.set(key, value);
    }

    public void add(Xkey xkey) {
        image.set(new Xkey(Suite.add(), null, true), xkey);
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
        return " [ " + label + " ]\n" + image.toString();
    }
}
