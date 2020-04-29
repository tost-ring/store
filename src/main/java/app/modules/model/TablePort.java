package app.modules.model;

public class TablePort extends Port {

    private int size;

    public TablePort(int size, String label) {
        super(label);
        this.size = size;
    }

    public TablePort(int size, Port port) {
        this(size, port.getLabel());
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int hashCode() {
        return getLabel().hashCode() + size;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TablePort && getLabel().equals(((TablePort) obj).getLabel()) && size == ((TablePort) obj).size;
    }

    @Override
    public String toString() {
        return "#[" + size + "]" + getLabel();
    }
}
