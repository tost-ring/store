package app.core.jorg;

import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import app.core.suite.WrapSubject;
import app.modules.graph.DupleGraph;
import app.modules.graph.HashDupleGraph;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

public class GeneralPerformer implements Performer{

    private DupleGraph<String, Class<?>> namingGraph;
    private Performer subPerformer;

    public GeneralPerformer(Subject naming) {
        namingGraph = new HashDupleGraph<>();
        for(String it : Suite.keys(naming, String.class)) {
            namingGraph.link(it, naming.get(it));
        }
        subPerformer = new ObjectPerformer();
    }

    public Performer getSubPerformer() {
        return subPerformer;
    }

    public void setSubPerformer(Performer subPerformer) {
        this.subPerformer = subPerformer;
    }

    public boolean isComplex(Object object) {
        return !isPrimitive(object);
    }

    public boolean isPrimitive(Object object) {
        return object == null || object instanceof String || object instanceof Integer;
    }

    @Override
    public Subject subjectively(Object object) {
        Class<?> type = object.getClass();
        if(type.isArray()){
            return arraySubjectively(object);
        } else {
            Subject subject = subPerformer.subjectively(object);
            if(subject != null) {
                return subject;
            } else if(Subjective.class.isAssignableFrom(type)) {
                return ((Subjective) object).toSubject();
            } else {
                throw new ClassCastException("Cant transform " + object + " to Subject");
            }
        }
    }

    private Subject arraySubjectively(Object object) {
        Subject subject = new WrapSubject();
        Class<?> type = object.getClass().getComponentType();
        if (type.isPrimitive()) {
            if (type == Integer.TYPE) {
                int[] a = (int[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Byte.TYPE) {
                byte[] a = (byte[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Long.TYPE) {
                long[] a = (long[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Float.TYPE) {
                float[] a = (float[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Double.TYPE) {
                double[] a = (double[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Short.TYPE) {
                short[] a = (short[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Character.TYPE) {
                char[] a = (char[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else if (type == Boolean.TYPE) {
                boolean[] a = (boolean[]) object;
                for(int i = 0;i < a.length;++i) {
                    subject.set("" + i, a[i]);
                }
            } else {
                throw new InternalError();
            }
        } else {
            Object[] a = (Object[]) object;
            for(int i = 0;i < a.length;++i) {
                subject.set("" + i, a[i]);
            }
        }
        return subject;
    }

    @Override
    public Object objectively(Object object, Subject subject) {
        Class<?> aClass = object.getClass();
        if(aClass.isArray()) {
            return arrayObjectively(object, subject);
        } else {
            Object result = subPerformer.objectively(object, subject);
            if(result != null) {
                return result;
            } else if(Subjective.class.isAssignableFrom(aClass)) {
                ((Subjective)object).fromSubject(subject);
                return object;
            } else {
                return null;
            }
        }
    }

    private Object arrayObjectively(Object object, Subject subject) {
        Class<?> type = object.getClass().getComponentType();
        if (type.isPrimitive()) {
            if (type == Integer.TYPE) {
                int[] a = (int[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Byte.TYPE) {
                byte[] a = (byte[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Long.TYPE) {
                long[] a = (long[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Float.TYPE) {
                float[] a = (float[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Double.TYPE) {
                double[] a = (double[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Short.TYPE) {
                short[] a = (short[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Character.TYPE) {
                char[] a = (char[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else if (type == Boolean.TYPE) {
                boolean[] a = (boolean[]) object;
                for(int i = 0;i < a.length;++i) {
                    a[i] = subject.god("" + i, null);
                }
            } else {
                return null;
            }
        } else {
            Object[] a = (Object[]) object;
            for(int i = 0;i < a.length;++i) {
                a[i] = subject.god("" + i, null);
            }
        }
        return object;
    }

    public String getType(Object object) {
        if(object == null)return namingGraph.getBlack(null);
        String type = namingGraph.getBlack(object.getClass());
        if(type == null) {
            type = object.getClass().getTypeName();
        }
        return type;
    }

    public boolean isArrayType(String type) {
        Class<?> aClass = namingGraph.getWhite(type);
        return aClass != null && aClass.isArray();
    }

    public Object formObject(String type) {
        try {
            Class<?> aClass = namingGraph.getWhite(type);
            if(aClass == null) {
                aClass = Class.forName(type);
            }
            if (aClass != null) {
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Object formArray(String type, int size) {
        try {
            Class<?> aClass = namingGraph.getWhite(type);
            if(aClass == null) {
                aClass = Class.forName(type);
            }
            if (aClass != null) {
                return Array.newInstance(aClass, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
