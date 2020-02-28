package app.core.jorg;

import java.util.Objects;

public class Xkey {
    private Object object;
    private String type;
    private String id;
    private boolean array;

    public Xkey(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public Xkey(Object o, String type, String id) {
        this.object = o;
        this.type = type;
        this.id = id;
    }

    public Xkey(String type, String id, boolean array) {
        this.type = type;
        this.id = id;
        this.array = array;
    }

    public void set(Xkey that) {
        this.object = that.object;
        this.type = that.type;
        this.id = that.id;
        this.array = that.array;

    }

    public Object getObject() {
        return object;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public boolean isArray() {
        return array;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    @Override
    public boolean equals(Object o1) {
        if(id == null) return Objects.equals(o1, object);
        String thatId = o1 instanceof Xkey ? ((Xkey) o1).id : o1 instanceof String ? (String)o1 : null;
        return id.equals(thatId);
    }

    @Override
    public int hashCode() {
        return id == null ? Objects.hashCode(object) : id.hashCode();
    }

    @Override
    public String toString() {
        if(object != null) {
            return "" + object;
        } else {
            return type + "@" + id;
        }
    }
}
