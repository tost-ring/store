package app.core.suite;

import app.controller.tool.ParentHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class Prospect {

    public static Subject classpathSubjectively(Subject subject) {
        Object object = subject.direct();
        Subject sub = Suite.set();
        for(Class<?> aClass = object.getClass(); aClass != Object.class; aClass = aClass.getSuperclass()) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                        field.setAccessible(true);
                        sub.put(field, field.get(object));
                    }
                } catch (Exception ex) {
                    System.err.println("Cant get '" + field.getName() + "' from " + field.getDeclaringClass());
                }
            }
        }
        return sub;
    }

    public static void classpathObjectively(Subject subject) {
        Object object = subject.get(Object.class).asExpected();
        Subject sub = subject.get(Subject.class).asExpected();
        for(Class<?> aClass = object.getClass(); aClass != Object.class; aClass = aClass.getSuperclass()) {
            try {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    if (sub.get(field.getName()).settled()) {
                        field.setAccessible(true);
                        field.set(object, sub.get(field.getName()).direct());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Subject collectionSubjectively(Subject subject) {
        Collection<?> collection = subject.prime().asExpected();
        Subject sub = Suite.set();
        int i = 0;
        for(Object it : collection) {
            sub.set(i++, it);
        }
        return sub;
    }

    public static void collectionObjectively(Subject subject) {
        Collection<?> collection = subject.get(Object.class).asExpected();
        Subject sub = subject.get(Subject.class).asExpected();
        for(Subject s : sub.front()) {
            collection.add(s.asExpected());
        }
    }

    public static Subject mapSubjectively(Subject subject) {
        Map<?, ?> map = subject.prime().asExpected();
        Subject sub = Suite.set();
        for(var entry : map.entrySet()) {
            sub.set(entry.getKey(), entry.getValue());
        }
        return sub;
    }

    public static void mapObjectively(Subject subject) {
        Map<?, ?> map = subject.get(Object.class).asExpected();
        Subject sub = subject.get(Subject.class).asExpected();
        for(Subject it : sub.front()) {
            map.put(it.key().asExpected(), it.asExpected());
        }
    }

    public static Subject fileSubjectively(Subject subject) {
        File file = subject.prime().asExpected();
        return Suite.set("path", file.getPath());
    }

}
