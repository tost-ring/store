package app.core.suite;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface Subjective {
    default Subject toSubject() {
        Subject subject = new WrapSubject();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    field.setAccessible(true);
                    subject.set(field.getName(), field.get(this));
                }
            } catch (Exception ex) {
                System.err.println("Cant get " + field.getName() + " from " + field.getType());
            }
        }
        return subject;
    }
    default void fromSubject(Subject subject) {
        Class<?> aClass = getClass();
        for (String name : Suite.keys(subject, String.class)) {
            try {
                Field field = aClass.getDeclaredField(name);
                if (field.trySetAccessible()) {
                    field.set(this, subject.get(name));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Cant set " + name + " in " + aClass);
            }
        }
    }
}
