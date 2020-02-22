package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Trigger<C, S> implements Impression {

    private Consumer<C> consumer;
    private Supplier<S> supplier;

    public Trigger(Consumer<C> consumer, Supplier<S> supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
    }

    @Override
    public void revel(Subject in) {
        consumer.accept(in.god(null));
    }

    @Override
    public void revel(Subject state, Subject in) {
        revel(in);
    }

    @Override
    public Subject play() {
        return Suite.set(supplier.get());
    }

    @Override
    public Subject play(Subject in) {
        revel(in);
        return Suite.set();
    }

    @Override
    public Subject play(Subject state, Subject in) {
        revel(in);
        return Suite.set();
    }

    @Override
    public Subject gamble() throws Exception {
        return play();
    }

    @Override
    public Subject gamble(Subject in) throws Exception {
        revel(in);
        return Suite.set();
    }

    @Override
    public Subject gamble(Subject state, Subject in) throws Exception {
        revel(in);
        return Suite.set();
    }
}
