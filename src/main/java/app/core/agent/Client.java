package app.core.agent;

import app.core.suite.Fun;
import app.core.suite.Subject;
import app.core.suite.Suite;

public abstract class Client {

    public static final Object source = new Object();

    final Subject order(Service service, Subject subject) {
        return Order.fulfil(service, subject.set(source, this));
    }

    final void order(Service service, Subject subject, Fun callback) {
        Order.fulfil(service, subject.set(source, this), callback);
    }

    final Subject order(Service service, Object object) {
        return Order.fulfil(service, Suite.set(source, this).set(object));
    }

    final void order(Service service, Object object, Fun callback) {
        Order.fulfil(service, Suite.set(source, this).set(object), callback);
    }
}
