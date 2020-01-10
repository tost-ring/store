package app.core.agent;

import app.core.suite.Fun;
import app.core.suite.Subject;

public class Order {

    public static Subject fulfil(Service service, Subject subject) {
        return service.fulfil(subject);
    }

    public static void fulfil(Service service, Subject subject, Fun callback) {
        new Thread(() -> callback.play(service.fulfil(subject))).start();
    }
}
