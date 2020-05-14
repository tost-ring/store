package app.core.jorg;

public class Reference {

    private Object label;

    public Reference(Object label) {
        this.label = label;
    }

    public Object getLabel() {
        return label;
    }

    public void setLabel(Object label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Reference && label.equals(((Reference) obj).label);
    }

    @Override
    public String toString() {
        return "@" + label;
    }
}
