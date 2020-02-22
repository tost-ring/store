package app.core.jorg;

import app.core.suite.*;
import app.core.suite.action.*;

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

    public<T> void addType(Class<T> type, Function subjectively, Impression objectively, Supplier<T> constructor) {
        subjectiveRouter.set(type, subjectively);
        objectiveRouter.set(type, objectively);
        constructRouter.set(type, Expression.fromSupplier(constructor));
    }

    @Override
    public Subject subjectively(Object object) {
        if(object instanceof Subjective) {
            return ((Subjective) object).toSubject();
        } else {
            Function function = subjectiveRouter.god(object.getClass(), null);
            if(function == null)return null;
            return function.play(Suite.set(object));
        }
    }

    @Override
    public boolean objectively(Object object, Subject subject) {
        if(object instanceof Subjective) {
            ((Subjective) object).fromSubject(subject);
        } else {
            Impression impression = objectiveRouter.god(object.getClass(), null);
            if(impression == null) return false;
            impression.revel(Suite.set(Object.class, object).set(Subject.class, subject));
        }
        return true;
    }

    @Override
    public Object construct(Class<?> type) {
        Expression expression = constructRouter.god(type, null);
        if(expression == null) return null;
        return expression.play().god(null);
    }
}
