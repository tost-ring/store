package app.modules.model.items;

import app.core.suite.Subjective;

import java.util.ArrayList;
import java.util.List;

public class Family implements Subjective {

    private Person father;
    private Person mather;
    private List<Person> children;

    public Family() {}

    public Family(Person father, Person mather) {
        this.father = father;
        this.mather = mather;
        children = new ArrayList<>();
    }

    public Person getFather() {
        return father;
    }

    public Person getMather() {
        return mather;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public void setMather(Person mather) {
        this.mather = mather;
    }

    @Override
    public String toString() {
        return "Ojciec: " + father
                + "\nMatka: " + mather
                + "\nDzieci: " + children + "\n";
    }
}
