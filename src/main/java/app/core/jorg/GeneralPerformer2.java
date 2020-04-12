package app.core.jorg;

import app.core.flow.FlowIterable;
import app.core.flow.Interable;
import app.core.suite.Graph;
import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import app.core.suite.action.Action;
import app.core.suite.action.DiceyAction;
import app.modules.model.Socket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class GeneralPerformer2 {

    private Subject constructors = Suite.set();
    private Subject initializers = Suite.set();
    private Subject adapters = Suite.set();

    public GeneralPerformer2() {
        this(true, true, true);
    }

    public GeneralPerformer2(boolean allowDefaultConstructors, boolean allowDefaultInitializers, boolean allowDefaultAdapters) {
        if(allowDefaultConstructors) {
            constructors.add((Action) s -> {
                if(s.prime().isIn(Class.class)) {
                    Class<?> type = s.prime().asExpected();
                    FlowIterable<Object> args = s.values().skip(0, 1);
                    try {
                        Constructor<?> c = type.getDeclaredConstructor(args.map(Object::getClass).toList().toArray(new Class<?>[0]));
                        Object newInstance = c.newInstance(s.values().skip(0, 1).toList().toArray());
                        return Suite.set(newInstance);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return Suite.set();
                    }
                } else if(s.size() == 1) {
                    return s;
                } else if(s.size() == 0) {
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

        //  Podłączenie gniazdka
        if(xkey.getLabel() instanceof Socket) {
            for(var adapter : adapters.reverse()) {
                Function<String, Object> function = adapter.asExpected();
                Object r = function.apply(((Socket) xkey.getLabel()).getSubject());
                if(r != null) {
                    xkey.setObject(r);
                    return r;
                }
            }
        }

        //  Konstrukcja obiektu
        var params = xkey.getDirect().front().map(s -> s.asGiven(Xkey.class)).
                map(this::construct).toSubject(Interable.indexes());

        for(var recipe : constructors.reverse()) {
            Action action = recipe.asExpected();
            var result = action.play(params);
            if(result.settled()) {
                xkey.setObject(result.direct());
                return result.direct();
            }
        }

        return null;
    }

    public void initialize(Xkey xkey) throws NoSuchMethodException {

        Graph params = new Graph();
        for(Xkey from : xkey.getGraph().keys().filter(Xkey.class)) {
            for(Xkey to : xkey.getGraph().get(from).values().filter(Xkey.class)) {
                params.add(from.getObject(), to.getObject());
            }
        }

        for (var initializer : initializers.reverse()) {
            BiPredicate<Object, Subject> predicate = initializer.asExpected();
            if (predicate.test(xkey.getObject(), params)) {
                return;
            }
        }

        throw new NoSuchMethodException();
    }
}
