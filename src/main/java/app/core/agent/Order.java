package app.core.agent;

import app.core.suite.action.Action;
import app.core.suite.Subject;

public class Order {

    public static Subject fulfil(Service service, Subject sub) {
        return service.fulfil(sub);
    }

    public static Thread fulfil(Service service, Subject sub, Action callback) {
        Thread thread = new Thread(() -> callback.play(service.fulfil(sub)));
        thread.start();
        return thread;
    }
}
