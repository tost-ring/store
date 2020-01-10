package app.core.suite;

@FunctionalInterface
public interface Tun extends Fun {
    Subject gamble(Subject subject) throws Exception;
    default Subject play(Subject subject) {
        try {
            return gamble(subject);
        } catch (Exception e) {
            e.printStackTrace();
            return Suite.set(Exception.class, e);
        }
    }
    default Subject play() {
        return play(Suite.set());
    }
}
