package app.core.jorg;

import app.core.suite.*;
import app.core.suite.transition.Function;

import java.util.function.Supplier;


public class DetailPerformer implements Performer{



    private Subject subjectiveRouter = Suite.
            set();

    private Subject objectiveRouter = Suite.
            set();

    private Subject constructRouter = Suite.
            set();

    public DetailPerformer() {
    }

    public Subject getSubjectiveRouter() {
        return subjectiveRouter;
    }

    public Subject getObjectiveRouter() {
        return objectiveRouter;
    }

    public Subject getConstructRouter() {
        return constructRouter;
    }

    public<T> void addType(Class<T> type, Function subjectively, Function objectively, Supplier<T> constructor) {
//        Suite.set("A", Suite::set);
//        subjectiveRouter.sor(type, () -> {});
        objectiveRouter.sor(type, objectively);
//        constructRouter.sor(type, Expression.fromSupplier(constructor)); TODO
    }

    @Override
    public Subject subjectively(Object object) {
        if(object instanceof Subjective) {
            return ((Subjective) object).toSubject();
        } else {
            return subjectiveRouter.act(object.getClass(), Suite.set(object));
        }
    }

    @Override
    public boolean objectively(Object object, Subject subject) {
        if(object instanceof Subjective) {
            return ((Subjective) object).fromSubject(subject).get();
        } else {
            return objectiveRouter.act(object.getClass(), Suite.set(Object.class, object).set(Subject.class, subject)).get();
        }
    }

    @Override
    public Object construct(Class<?> type) {
        return constructRouter.aod(type, ZeroSubject.getInstance()).god(null);
    }
}
