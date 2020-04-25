package app.core.jorg;

public class GeneralPerformer /*implements Performer*/{
//
//    private DupleGraph<String, Class<?>> namingGraph;
//    private Performer detailPerformer;
//
//    public GeneralPerformer(Subject aliases) {
//        namingGraph = new HashDupleGraph<>();
//        for(var it : aliases) {
//            if(it.is(Class.class) && it.key().is(String.class)) {
//                namingGraph.link(it.key().asExpected(), it.asExpected());
//            }
//            if(it.is(String.class) && it.key().is(Class.class)) {
//                namingGraph.link(it.asExpected(), it.key().asExpected());
//            }
//        }
//        detailPerformer = DefaultDetailPerformer.getInstance();
//    }
//
//    public Performer getDetailPerformer() {
//        return detailPerformer;
//    }
//
//    public void setDetailPerformer(Performer performer) {
//        this.detailPerformer = performer;
//    }
//
//    public boolean isComplex(Object object) {
//        return !isPrimitive(object);
//    }
//
//    public boolean isPrimitive(Object object) {
//        return object == null || object instanceof String || object instanceof Integer ||
//                object instanceof Double || object instanceof Field || object instanceof Class;
//    }
//
//    @Override
//    public Subject subjectively(Object object) {
//        Class<?> type = object.getClass();
//        if(type.isArray()){
//            return arraySubjectively(object);
//        } else {
//            Subject subject = detailPerformer.subjectively(object);
//            if(subject != null) {
//                return subject;
//            } else if(Subjective.class.isAssignableFrom(type)) {
//                return ((Subjective) object).toSubject();
//            } else {
//                throw new ClassCastException("Cant transform " + object.getClass() + " to Subject");
//            }
//        }
//    }
//
//    private Subject arraySubjectively(Object object) {
//        Subject subject = Suite.set();
//        Class<?> type = object.getClass().getComponentType();
//        if (type.isPrimitive()) {
//            if (type == Integer.TYPE) {
//                int[] a = (int[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Byte.TYPE) {
//                byte[] a = (byte[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Long.TYPE) {
//                long[] a = (long[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Float.TYPE) {
//                float[] a = (float[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Double.TYPE) {
//                double[] a = (double[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Short.TYPE) {
//                short[] a = (short[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Character.TYPE) {
//                char[] a = (char[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else if (type == Boolean.TYPE) {
//                boolean[] a = (boolean[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    subject.set("" + i, a[i]);
//                }
//            } else {
//                throw new InternalError();
//            }
//        } else {
//            Object[] a = (Object[]) object;
//            for(int i = 0;i < a.length;++i) {
//                subject.set("" + i, a[i]);
//            }
//        }
//        return subject;
//    }
//
//    @Override
//    public boolean objectively(Object object, Subject subject) {
//        Class<?> aClass = object.getClass();
//        if(aClass.isArray()) {
//            return arrayObjectively(object, subject);
//        } else {
//            return detailPerformer.objectively(object, subject);
//        }
//    }
//
//    private boolean arrayObjectively(Object object, Subject subject) {
//        Class<?> type = object.getClass().getComponentType();
//        if (type.isPrimitive()) {
//            if (type == Integer.TYPE) {
//                int[] a = (int[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Byte.TYPE) {
//                byte[] a = (byte[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Long.TYPE) {
//                long[] a = (long[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Float.TYPE) {
//                float[] a = (float[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Double.TYPE) {
//                double[] a = (double[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Short.TYPE) {
//                short[] a = (short[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Character.TYPE) {
//                char[] a = (char[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else if (type == Boolean.TYPE) {
//                boolean[] a = (boolean[]) object;
//                for(int i = 0;i < a.length;++i) {
//                    a[i] = subject.get("" + i).orSpare(null);
//                }
//            } else {
//                return false;
//            }
//        } else {
//            Object[] a = (Object[]) object;
//            for(int i = 0;i < a.length;++i) {
//                a[i] = subject.get("" + i).direct();
//            }
//        }
//        return true;
//    }
//
//    public String getTypeAlias(Object object) {
//        if(object == null)return namingGraph.getBlack(null);
//        return getAlias(object.getClass());
//    }
//
//    public String getAlias(Class<?> type) {
//        String alias = namingGraph.getBlack(type);
//        if(alias == null) {
//            alias = type.getTypeName();
//        }
//        return alias;
//    }
//
//    public Class<?> getType(String typeName) {
//        Class<?> type = namingGraph.getWhite(typeName);
//        if(type != null)return type;
//        try {
//            return Class.forName(typeName);
//        } catch (ClassNotFoundException e) {
//            return null;
//        }
//    }
//
//    public boolean isArrayType(String type) {
//        Class<?> aClass = namingGraph.getWhite(type);
//        return aClass != null && aClass.isArray();
//    }
//
//    @Override
//    public Object construct(Class<?> type) {
//        try {
//            Object object = detailPerformer.construct(type);
//            if(object != null)return object;
//            Constructor<?> constructor = type.getDeclaredConstructor();
//            constructor.setAccessible(true);
//            return constructor.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Object formObject(Subject sub) {
//        try {
//            Socket socket = sub.keys().filter(Socket.class).iterator().next(null);
//            if(socket != null) {
//                Class<?> type = namingGraph.getWhite(socket.getSubject());
//                if (type == null) {
//                    type = Class.forName(socket.getSubject());
//                }
//                if (type != null) {
//                    return construct(type);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Object formArray(String type, int size) {
//        try {
//            Class<?> aClass = namingGraph.getWhite(type);
//            if(aClass == null) {
//                aClass = Class.forName(type);
//            }
//            if (aClass != null) {
//                return Array.newInstance(aClass, size);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }
}
