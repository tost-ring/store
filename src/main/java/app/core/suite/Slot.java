package app.core.suite;

public interface Slot {
    Slot PRIME = new Slot(){};
    Slot RECENT = new Slot(){};

    static Slot before(Object key) {
        return new SlotBefore(key);
    }

    static Slot after(Object key) {
        return new SlotAfter(key);
    }

    class SlotBefore implements Slot {
        Object key;

        SlotBefore(Object key) {
            this.key = key;
        }
    }

    class SlotAfter implements Slot {
        Object key;

        SlotAfter(Object key) {
            this.key = key;
        }
    }
}
