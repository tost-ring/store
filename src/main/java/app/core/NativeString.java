package app.core;

import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class NativeString implements Subjective {

    private Subject translations = Suite.set();
    private StringProperty value = new SimpleStringProperty();

    public NativeString() {}

    @Override
    public Subject toSubject() {
        return translations;
    }

    @Override
    public void fromSubject(Subject sub) {
        translations.met(sub);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setNation(String nation) {
        this.value.set(translations.is(nation) ? translations.get(nation) : translations.god(""));
    }

    @Override
    public String toString() {
        return value.get();
    }
}
