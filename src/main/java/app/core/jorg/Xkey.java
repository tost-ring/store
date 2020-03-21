package app.core.jorg;

import java.util.Objects;

public class Xkey {
    private Object object;
    private String id;

    public Xkey(Object object, String id) {
        this.object = object;
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Xkey && Objects.equals(object, ((Xkey) o).object) && Objects.equals(id, ((Xkey) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(object) ^ Objects.hashCode(id);
    }

    @Override
    public String toString() {
        String str = "";
        if(object != null) {
            str += object;
        }
        if(id != null) {
            str += "@" + id;
        }
        return str;
    }
}
