package app.core.agent;

import app.core.suite.action.Action;
import app.core.suite.Subject;

public class Order {

    public static Subject fulfil(Service service, Subject subject) {
        return service.fulfil(subject);
    }

    public static void fulfil(Service service, Subject subject, Action callback) {
        new Thread(() -> callback.play(service.fulfil(subject))).start();
    }
}
