package app.modules.model;

import java.io.Serializable;

public class Item implements Serializable {

    private String name;
    private Item parent;

    public Item(String name, Item parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public boolean isComponentOf(Item composite, boolean direct) {
        return parent != null && (composite.equals(parent) || (!direct && parent.isComponentOf(composite, false)));
    }

    public boolean isInPathOf(Item item) {
        return equals(item) || isComponentOf(item, true);
    }


    @Override
    public String toString() {
        return name;
    }
}
