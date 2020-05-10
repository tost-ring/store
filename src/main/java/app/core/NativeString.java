package app.core;

import app.core.jorg.Performable;
import app.core.jorg.Reformable;
import app.core.suite.Subject;
import app.core.suite.Suite;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class NativeString implements Performable, Reformable {

    private Subject translations = Suite.set();
    private StringProperty value = new SimpleStringProperty();

    public NativeString() {}

    @Override
    public Subject perform() {
        return translations;
    }

    @Override
    public void reform(Subject sub) {
        translations.insetAll(sub.front());
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setNation(String nation) {
        this.value.set(translations.get(nation).orGiven(translations.prime().orGiven("???")));
    }

    @Override
    public String toString() {
        return value.get();
    }
}
