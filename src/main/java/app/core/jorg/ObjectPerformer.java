package app.core.jorg;

import app.core.suite.Fun;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.WrapSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectPerformer implements Performer{

    private static Subject subjectize = Suite.
            set(ArrayList.class, ObjectPerformer::listSubject);
    private static Subject objectize = Suite.
            set(ArrayList.class, ObjectPerformer::listObject);

    @Override
    public Subject subjectively(Object object) {
        Fun fun = subjectize.god(object.getClass(), null);
        if(fun == null) return null;
        return fun.play(Suite.set(object));
    }

    @Override
    public Object objectively(Object object, Subject subject) {
        Fun fun = objectize.god(object.getClass(), null);
        if(fun == null) return null;
        return fun.play(Suite.set(Object.class, object).set(Subject.class, subject)).get();
    }

    private static Subject listSubject(Subject subject) {
        List<?> list = subject.get();
        WrapSubject res = new WrapSubject();
        for(int i = 0;i < list.size(); ++i) {
            res.set("" + i, list.get(i));
        }
        return res;
    }

    private static Subject listObject(Subject subject) {
        List<Object> list = subject.get(Object.class);
        Subject s = subject.get(Subject.class);
        for(int i = 0;s.is("" + i);++i) {
            list.add(s.get());
        }
        return Suite.set(list);
    }
}
