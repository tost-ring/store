package app.core.agent;

import app.core.suite.action.Action;
import app.core.suite.Subject;

public abstract class Agent extends Broker {

    private Service boss;

    public Agent() {
    }

    public Agent(Service boss) {
        this.boss = boss;
    }

    public Service getBoss() {
        return boss;
    }

    protected void setBoss(Service boss) {
        this.boss = boss;
    }

    protected final Subject order(Subject subject) {
        return order(boss, subject);
    }

    protected final void order(Subject subject, Action callback) {
        order(boss, subject, callback);
    }

    protected final Subject order(Object object) {
        return order(boss, object);
    }

    protected final void order(Object object, Action callback) {
        order(boss, object, callback);
    }

    @Override
    public Subject fulfil(Subject subject) {
        return boss.fulfil(subject);
    }
}
