package app.core.jorg;

import app.core.fluid.Cascade;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.Port;
import app.modules.model.TablePort;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class JorgPerformer {

    private final Subject solvents = Suite.set();
    private final Subject ports = Suite.set();

    public JorgPerformer() {
        this(true, true);
    }

    public JorgPerformer(boolean allowDefaultSolvents, boolean allowDefaultPorts) {
        if(allowDefaultSolvents) {
            solvents.add((Function<Object, Subject>) o -> {
                Subject s = simple(o);
                if(s.settled())return s;
                if(o instanceof Fusible) {
                    return ((Fusible) o).melt();
                } else if(o.getClass().isArray()) {
                    return meltArray(o);
                } else if(o instanceof Collection) {
                    return StandardSolvent.meltCollection((Collection<?>) o);
                } else if(o instanceof Map) {
                    return StandardSolvent.meltMap((Map<?, ?>) o);
                } else if(o instanceof File) {
                    return StandardSolvent.meltFile((File)o);
                }
                return null;
            });
        }

        if(allowDefaultPorts) {
            ports.add((Function<Object, String>) o -> {
                if(o instanceof Class) {
                    return ((Class<?>) o).getName();
                }
                return null;
            });
        }
    }

    public void addSolvent(Function<Object, Subject> solvent) {
        solvents.add(solvent);
    }

    public void addPort(Function<Object, String> port) {
        ports.add(port);
    }

    public void addPort(Object object, String id) {
        ports.add((Function<Object, String>) o -> o == object ? id : null);
    }

    public Cascade<Crystal> melt(Subject solid) throws JorgWriteException {
        Subject solution = Suite.set();
        int automaticIndex = 0;

        for(var s : solid.front()) {

            Crystal germ = new Crystal(s.key().asExpected(), s.direct());
            Subject sub = solution.get(s.direct());
            if(sub.settled()) {
                Crystal crystal = sub.asExpected();
                germ.getBody().add(crystal);
                germ.setReady(true);
            }

            solution.set(germ);

        }

        for(Crystal c : solution.front().keys().filter(Crystal.class).filter(c -> !c.isReady())) {
            Object o = c.getGerm();
            for(var solvent : solvents.reverse()) {
                Function<Object, Subject> function = solvent.asExpected();
                Subject sub = function.apply(o);
                if(sub != null) {
                    for(var s : sub.front()) {
                        Crystal keyGerm;
                        Subject simple = simple(s.key().direct());
                        if(simple.settled()) {
                            keyGerm = Crystal.init(simple.direct());
                        } else {
                            Subject s1 = solution.get(s.key().direct());
                            if(s1.settled()) {
                                keyGerm = s1.asExpected();
                            } else {
                                keyGerm = new Crystal("" + ++automaticIndex, s.key().direct());
                                solution.set(keyGerm);
                            }
                        }

                        Crystal germ;
                        simple = simple(s.direct());
                        if(simple.settled()) {
                            germ = Crystal.init(simple.direct());
                        } else {
                            Subject s1 = solution.get(s.direct());
                            if(s1.settled()) {
                                germ = s1.asExpected();
                            } else {
                                germ = new Crystal("" + ++automaticIndex, s.direct());
                                solution.set(germ);
                            }
                        }
                        c.getBody().set(keyGerm, germ);
                    }
                    c.setReady(true);
                    break;
                }
            }
            if(!c.isReady()) throw new JorgWriteException("Uninitialized: " + c);
        }

        return solution.front().keys().filter(Crystal.class).cascade();
    }

    private Subject simple(Object o) {
        if(o == null) return Suite.add(null);
        if(o instanceof Port) return Suite.add(o);
        Port port = port(o);
        if(port != null) return Suite.add(port);
        if(o instanceof Boolean || o instanceof Character || o instanceof Byte || o instanceof Short ||
                o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double ||
                o instanceof String || o instanceof Suite.Add) {
            return Suite.add(o);
        }
        return Suite.set();
    }

    private Port port(Object o) {
        for(var port : ports.reverse()) {
            Function<Object, String> function = port.asExpected();
            String str = function.apply(o);
            if(str != null)return new Port(str);
        }
        return null;
    }

    private Subject meltArray(Object o) {
        Class<?> type = o.getClass().getComponentType();
        Port port = port(type);
        if(port == null) return null;
        TablePort tablePort = new TablePort(0, port);
        Subject s = Suite.add(tablePort);

        if (type.isPrimitive()) {
            if (type == Integer.TYPE) {
                for(var i : (int[]) o)s.add(i);
            } else if (type == Byte.TYPE) {
                for(var i : (byte[]) o)s.add(i);
            } else if (type == Long.TYPE) {
                for(var i : (long[]) o)s.add(i);
            } else if (type == Float.TYPE) {
                for(var i : (float[]) o)s.add(i);
            } else if (type == Double.TYPE) {
                for(var i : (double[]) o)s.add(i);
            } else if (type == Short.TYPE) {
                for(var i : (short[]) o)s.add(i);
            } else if (type == Character.TYPE) {
                for(var i : (char[]) o)s.add(i);
            } else if (type == Boolean.TYPE) {
                for(var i : (boolean[]) o)s.add(i);
            } else {
                throw new InternalError();
            }
        } else {
            for(var i : (Object[]) o)s.add(i);
        }
        tablePort.setSize(s.size() - 1);
        return s;
    }
}
