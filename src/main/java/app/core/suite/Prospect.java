package app.core.suite;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

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
                        sub.sos(field.getName(), field.get(object));
                    }
                } catch (Exception ex) {
                    System.err.println("Cant get '" + field.getName() + "' from " + field.getDeclaringClass());
                }
            }
        }
        return sub;
    }

    public static Subject classpathObjectively(Subject subject) {
        Object object = subject.get(Object.class);
        Subject sub = subject.get(Subject.class);
        for(Class<?> aClass = object.getClass(); aClass != Object.class; aClass = aClass.getSuperclass()) {
            try {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    if (sub.is(field.getName())) {
                        field.setAccessible(true);
                        field.set(object, sub.get());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Suite.set(false);
            }
        }
        return Suite.set(true);
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

    public static Subject collectionObjectively(Subject subject) {
        Collection<?> collection = subject.getAs(Object.class, Collection.class);
        Subject sub = subject.get(Subject.class);
        for(int i = 0;sub.is(i);++i) {
            if(!collection.add(sub.get()))
                return Suite.set(false);
        }
        return Suite.set(true);
    }

    public static Subject fileSubjectively(Subject subject) {
        File file = subject.get();
        return Suite.set("path", file.getPath());
    }

    public static Subject fileObjectively(Subject subject) {
        File file = subject.getAs(Object.class, File.class);
        Subject sub = subject.get(Subject.class);
        if(sub.is("path")) {

        }
        return Suite.set(true);
    }

}
