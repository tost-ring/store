package app.core.jorg;

public class Port {

    private String label;

    public Port(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Port && label.equals(((Port) obj).label);
    }

    @Override
    public String toString() {
        return "#" + label;
    }
}
