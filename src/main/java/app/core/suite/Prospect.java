package app.core.suite;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class Prospect {

    public static Subject classpathSubjectively(Subject subject) {
        Object object = subject.get();
        Subject sub = Suite.set();
        for(Class<?> aClass = object.getClass(); aClass != Object.class; aClass = aClass.getSuperclass()) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                        field.setAccessible(true);
                        sub.sos(field, field.get(object));
                    }
                } catch (Exception ex) {
                    System.err.println("Cant get '" + field.getName() + "' from " + field.getDeclaringClass());
                }
            }
        }
        return sub;
    }

    public static void classpathObjectively(Subject subject) {
        Object object = subject.get(Object.class);
        Subject sub = subject.get(Subject.class);
        for(Class<?> aClass = object.getClass(); aClass != Object.class; aClass = aClass.getSuperclass()) {
            try {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    if (sub.is(field.getName())) {
                        field.setAccessible(true);
                        field.set(object, sub.get(field.getName()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Subject collectionSubjectively(Subject subject) {
        Collection<?> collection = subject.get();
        Subject sub = Suite.set();
        int i = 0;
        for(Object it : collection) {
            sub.set(i++, it);
        }
        return sub;
    }

    public static void collectionObjectively(Subject subject) {
        Collection<?> collection = subject.getAs(Object.class, Collection.class);
        Subject sub = subject.get(Subject.class);
        for(int i = 0;sub.is(i);++i) {
            collection.add(sub.get(i));
        }
    }

    public static Subject mapSubjectively(Subject subject) {
        Map<?, ?> map = subject.get();
        Subject sub = Suite.set();
        for(Map.Entry<?, ?> entry : map.entrySet()) {
            sub.set(entry.getKey(), entry.getValue());
        }
        return sub;
    }

    public static void mapObjectively(Subject subject) {
        Map<Object, Object> map = subject.getAs(Object.class, Glass.map(Object.class, Object.class));
        Subject sub = subject.get(Subject.class);
        for(Object key : sub.keys()) {
            map.put(key, sub.get(key));
        }
    }

    public static Subject fileSubjectively(Subject subject) {
        File file = subject.get();
        return Suite.set("path", file.getPath());
    }

}
