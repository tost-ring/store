package app.core.jorg;

import app.controller.tool.ParentHelper;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class StandardSolvent {

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

    public static Subject meltCollection(Collection<?> collection) {
        Subject sub = Suite.add(collection.getClass()).set(null);
        collection.forEach(sub::add);
        return sub;
    }

    public static Subject meltMap(Map<?, ?> map) {
        Subject sub = Suite.add(map.getClass());
        for(var entry : map.entrySet()) {
            sub.set(entry.getKey(), entry.getValue());
        }
        return sub;
    }

    public static Subject meltFile(File file) {
        return Suite.add(File.class).add(file.getPath());
    }

}
