package app.core.suite;

import app.core.flow.FlowCollection;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableColumn;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Glass<C, G extends C> implements Serializable {

    abstract G cast(Object o);
    abstract Glass[] getGenerics();

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
            Glass[] getGenerics() {
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
            Glass[] getGenerics() {
                return new Glass[]{brand};
            }
        };
    }

    public static<B> Glass<FlowCollection, FlowCollection<B>> flowCollection(Class<B> brand) {
        return flowCollection(Glass.of(brand));
    }

    public static<B> Glass<FlowCollection, FlowCollection<B>> flowCollection(Glass<B, B> brand) {
        return new Glass<>(FlowCollection.class) {

            @Override
            public FlowCollection<B> cast(Object o) {
                return (FlowCollection<B>)o;
            }

            @Override
            Glass[] getGenerics() {
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
            Glass[] getGenerics() {
                return new Glass[]{Glass.of(a), Glass.of(b)};
            }
        };
    }
}
