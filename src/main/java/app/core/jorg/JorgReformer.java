package app.core.jorg;

import app.core.jorg.util.PortableList;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static app.core.jorg.Jorg.terminator;

public class JorgReformer {

    private final Subject constructors = Suite.set();
    private final Subject reformers = Suite.set();
    private final Subject adapters = Suite.set();

    public JorgReformer() {
        this(true, true);
    }

    public JorgReformer(boolean enableStandardReformers, boolean enableDefaultAdapters) {

        if(enableStandardReformers) {
            reformers.insetAll(StandardReformer.getAllSupported().front());
        }

        if(enableDefaultAdapters) {
            adapters.set("list", PortableList.class);
        }
    }


    public void addConstructor(Function<Subject, Object> constructor) {
        constructors.add(constructor);
    }

    public<T> void setReformer(Class<T> type, BiConsumer<T, Subject> reformer) {
        reformers.set(type, reformer);
    }

    public void setAdapter(String s, Object o) {
        adapters.set(s, o);
    }

    protected Object construct(Xkey xkey) throws JorgReadException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(xkey.isConstructed())return xkey.getObject();
        Object o;

        //  Podłączenie do portu
        if(xkey.getLabel() instanceof Port) {
            Port port = (Port)xkey.getLabel();
            Subject sub = adapters.get(port.getLabel());
            if(sub.settled()) {
                o = sub.direct();
            } else {
                try {
                    o = Class.forName(port.getLabel());
                } catch (ClassNotFoundException e) {
                    throw new JorgReadException("No adapter found for port " + port);
                }
            }
            xkey.setObject(o);
            xkey.setConstructed(true);
            return o;
        }

        //  Konstrukcja obiektu
        xkey.setUnderConstruction(true);

        Subject image = xkey.getImage();
        Subject params = Suite.set();
        int paramIndex = 0;
        // Parametry konstukcyjne to parametry do pierwszego oznaczonego lub do termiantora
        for(var s : image.front()) {
            Xkey key = s.key().asExpected();
            if(key.getObject() instanceof Suite.Add) {
                Xkey value = s.asExpected();
                if(value.isUnderConstruction()) throw new JorgReadException("Construction loop");
                if(!value.isConstructed()) construct(value);
                image.take(key);
                if(value.getObject() == terminator) break;
                params.set(paramIndex++, value.getObject());
            } else {
                if(key.getObject() == terminator) {
                    image.take(key);
                }
                break;
            }
        }

        xkey.setUnderConstruction(false);

        for(var s : constructors.reverse()) {
            Function<Subject, Object> constructor = s.asExpected();
            o = constructor.apply(params);
            if(o != null) {
                xkey.setObject(o);
                xkey.setConstructed(true);
                return o;
            }
        }

        if(params.size() == 1) {
            if (params.assigned(Class.class)) {
                Class<?> type = params.asExpected();
                Constructor<?> c = type.getDeclaredConstructor();
                o = c.newInstance();
            } else if (image.size() == 0) {
                o = params.direct();
            } else {
                o = Suite.addAll(params.front().values());
            }
        } else if(params.size() == 2) {
            if(params.get(0).assigned(Class.class) && params.get(1).assigned(Integer.class)) {
                o = Array.newInstance(params.get(0).asExpected(), params.get(1).asExpected());
            } else {
                o = Suite.addAll(params.front().values());
            }
        } else {
            o = Suite.addAll(params.front().values());
        }
        xkey.setObject(o);
        xkey.setConstructed(true);

        return o;
    }

    protected void reform(Xkey xkey) throws JorgReadException {

        Object o = xkey.getObject();
        if(o == null || o instanceof Boolean || o instanceof Character || o instanceof Byte || o instanceof Short ||
                o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double ||
                o instanceof String) return;

        Subject params = Suite.insetAll(xkey.getImage().front().
                map(s -> Suite.set(s.key().asGiven(Xkey.class).getObject(), s.asGiven(Xkey.class).getObject())));

        if(o instanceof Reformable) {
            ((Reformable) o).reform(params);
        } else if(o.getClass().isArray()) {
            reformArray(o, params);
        } else {
            Subject sub = reformers.get(o.getClass());
            if(sub.settled()) {
                BiConsumer<Object, Subject> reformer = sub.asExpected();
                reformer.accept(o, params);
            } else throw new JorgReadException("Reformer for " + xkey.getObject() + " #" + xkey.getObject().getClass() + " not found");
        }
    }

    protected void reformArray(Object o, Subject s) {
        Object[] a = (Object[]) o;
        int i = 0;
        for(var sub : s.front()) {
            if(sub.key().assigned(Integer.class)) {
                i = sub.key().asExpected();
            }
            if(i < a.length)a[i++] = sub.direct();
        }
    }
}
