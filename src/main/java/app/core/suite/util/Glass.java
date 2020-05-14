package app.core.suite.util;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableColumn;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Glass<C, G extends C> implements Serializable {

    public abstract G cast(Object o);
    public abstract Glass[] getGenerics();

    private Class<C> c;

    public Glass(Class<C> c) {
        this.c = c;
    }

    public boolean isInstance(Object o) {
        return c.isInstance(o);
    }

    Class<C> getMainClass() {
        return c;
    }

    public int hashCode() {
        return c.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Glass && ((Glass) obj).getMainClass().equals(getMainClass())
                && Arrays.equals(((Glass) obj).getGenerics(), getGenerics());
    }

    @Override
    public String toString() {
        return getMainClass() + "<" + Arrays.toString(getGenerics()) + ">";
    }

    public static<B> Glass<B, B> of(Class<B> brand) {
        return new Glass<>(brand) {

            @Override
            public B cast(Object o) {
                return getMainClass().cast(o);
            }

            @Override
            public Glass[] getGenerics() {
                return new Glass[0];
            }
        };
    }

    public static<B> Glass<ObjectProperty, ObjectProperty<B>> objectProperty(Class<B> brand) {
        return objectProperty(Glass.of(brand));
    }

    public static<B> Glass<ObjectProperty, ObjectProperty<B>> objectProperty(Glass<B, B> brand) {
        return new Glass<>(ObjectProperty.class) {

            @Override
            public ObjectProperty<B> cast(Object o) {
                return (ObjectProperty<B>)o;
            }

            @Override
            public Glass[] getGenerics() {
                return new Glass[]{brand};
            }
        };
    }

    public static<A, B> Glass<TableColumn, TableColumn<A, B>> tableColumn(Class<A> a, Class<B> b) {
        return new Glass<>(TableColumn.class) {

            @Override
            public TableColumn<A, B> cast(Object o) {
                return (TableColumn<A, B>)o;
            }

            @Override
            public Glass[] getGenerics() {
                return new Glass[]{Glass.of(a), Glass.of(b)};
            }
        };
    }

    public static<A, B> Glass<Map, Map<A, B>> map(Class<A> a, Class<B> b) {
        return new Glass<>(Map.class) {

            @Override
            public Map<A, B> cast(Object o) {
                return (Map<A, B>)o;
            }

            @Override
            public Glass[] getGenerics() {
                return new Glass[]{Glass.of(a), Glass.of(b)};
            }
        };
    }
}
