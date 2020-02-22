package app.modules.model;

import app.core.flow.FlowArrayList;
import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import app.modules.model.items.Capacitor;
import app.modules.model.items.Storable;
import app.modules.model.items.StorableStaff;

public class Store implements Subjective {

    private Subject categoryTree = Suite.set();
    private FlowArrayList<Storable> stored = new FlowArrayList<>();
    private FlowArrayList<StorableStaff> elementsTypes = new FlowArrayList<>(
            new Capacitor.CapacitorStaff()
    );

    public Store() {}

    public Store(Subject categoryTree) {
        this.categoryTree = categoryTree;
    }

    public Subject getCategoryTree() {
        return categoryTree;
    }

    public FlowArrayList<Storable> getStored() {
        return stored;
    }

    public FlowArrayList<StorableStaff> getElementsTypes() {
        return elementsTypes;
    }
}
