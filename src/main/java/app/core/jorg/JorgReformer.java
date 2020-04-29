package app.core.jorg;

import app.core.fluid.Interable;
import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import app.core.suite.action.Action;
import app.core.suite.action.DiceyAction;
import app.modules.model.Port;
import app.modules.model.TablePort;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class JorgReformer {

    private final Subject constructors = Suite.set();
    private final Subject initializers = Suite.set();
    private final Subject adapters = Suite.set();

    public JorgReformer() {
        this(true, true, true);
    }

    public JorgReformer(boolean allowDefaultConstructors, boolean allowDefaultInitializers, boolean allowDefaultAdapters) {
        if(allowDefaultConstructors) {
            constructors.add((DiceyAction) s -> {
                if(s.size() == 1) {
                    if(s.assigned(Class.class)) {
                        Class<?> type = s.asExpected();
                        Constructor<?> c = type.getDeclaredConstructor();
                        Object newInstance = c.newInstance();
                        return Suite.set(newInstance);
                    } else {
                        return s; // Prymitywy i tablice bezposrednio zwrocic w subiekcie
                    }
                } else if(s.size() == 0) { // Domyslna klasa jest Subject
                    return Suite.set(Suite.set());
                }
                return Suite.set();
            });
        }

        if(allowDefaultInitializers) {
            initializers.add((BiPredicate<Object, Subject>) (o, s) -> {
                if(o instanceof Subjective) {
                    ((Subjective) o).fromSubject(s);
                    return true;
                } else if(o.getClass().isArray()) {
                    return initializeArray(o, s);
                }
                return !s.settled();
            });
        }

        if(allowDefaultAdapters) {
            adapters.add((Function<String, Object>) s -> {
                try {
                    return Class.forName(s);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        }
    }


    public void addConstructor(DiceyAction action) {
        constructors.add(action);
    }

    public void addInitializer(BiPredicate<Object, Subject> initializer) {
        initializers.add(initializer);
    }

    public void addAdapter(Function<String, Object> adapter) {
        adapters.add(adapter);
    }

    public Object construct(Xkey xkey) {
        if(xkey.getObject() != null)return xkey.getObject();

        //  Podłączenie do portu
        if(xkey.getLabel() instanceof Port) {
            Port port = (Port)xkey.getLabel();
            for(var adapter : adapters.reverse()) {
                Function<String, Object> function = adapter.asExpected();
                Object r = function.apply(port.getLabel());
                if(r != null) {
                    if(port instanceof TablePort) {
                        if(!(r instanceof Class)) throw new IllegalArgumentException("Non class port given as TablePort source");
                        r = Array.newInstance((Class<?>)r, ((TablePort) port).getSize());
                    }
                    xkey.setObject(r);
                    return r;
                }
            }
            throw new NullPointerException();
        }

        //  Konstrukcja obiektu
        var params = xkey.getPre().front().values().filter(Xkey.class).
                map(this::construct).toSubject(Interable.indexes());

        for(var recipe : constructors.reverse()) {
            Action action = recipe.asExpected();
            var result = action.play(params);
            if(result.settled()) {
                xkey.setObject(result.direct());
                return result.direct();
            }
        }

        throw new NullPointerException();
    }

    public void initialize(Xkey xkey) {

        Subject params = Suite.set();
        params.setAll(xkey.getPost().front().map(s -> {
            if(s.key().assigned(Xkey.class)) {
                return Suite.set(s.key().asGiven(Xkey.class).getObject(), s.asGiven(Xkey.class).getObject());
            } else {
                return Suite.set(s.key().direct(), s.asGiven(Xkey.class).getObject());
            }
        }));

        for (var initializer : initializers.reverse()) {
            BiPredicate<Object, Subject> predicate = initializer.asExpected();
            if (predicate.test(xkey.getObject(), params)) {
                return;
            }
        }

        throw new NullPointerException();
    }

    private boolean initializeArray(Object o, Subject s) {
        Object[] a = (Object[]) o;
        int i = 0;
        for(var sub : s.front()) {
            if(sub.key().assigned(Integer.class)) {
                i = sub.key().asExpected();
            }
            if(i < a.length)a[i++] = sub.direct();
        }
        return true;
    }
}
