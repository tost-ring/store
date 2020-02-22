package app.core.agent;

import app.core.suite.action.Function;
import app.core.suite.Subject;
import app.core.suite.Suite;

public abstract class Client {

    public static final Object source = new Object();

    final Subject order(Service service, Subject subject) {
        return Order.fulfil(service, subject.sos(source, this));
    }

    final void order(Service service, Subject subject, Function callback) {
        Order.fulfil(service, subject.sos(source, this), callback);
    }

    final Subject order(Service service, Object object) {
        return Order.fulfil(service, Suite.set(source, this).set(object));
    }

    final void order(Service service, Object object, Function callback) {
        Order.fulfil(service, Suite.set(source, this).set(object), callback);
    }
}
