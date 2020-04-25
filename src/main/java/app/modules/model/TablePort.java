package app.modules.model;

public class TablePort {

    private int size;
    private String label;

    public TablePort(int size, String label) {
        this.size = size;
        this.label = label;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        return label.hashCode() + size;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TablePort && label.equals(((TablePort) obj).label) && size == ((TablePort) obj).size;
    }

    @Override
    public String toString() {
        return "#[" + size + "]" + label;
    }
}
