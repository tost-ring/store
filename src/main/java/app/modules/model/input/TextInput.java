package app.modules.model.input;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.Values;

import java.text.ParseException;

public class TextInput implements Input {

    private String label;

    public TextInput() {
        this("");
    }

    public TextInput(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Subject valueFrom(Subject subject) {
        if(subject.is(this)) {
            try {
                return Suite.set("value", Values.electronicParse(subject.get()));
            } catch(ParseException pe) {
                return Suite.error(pe.getMessage());
            }
        }
        return Suite.error("Wartość jest wymagana");
    }
}
