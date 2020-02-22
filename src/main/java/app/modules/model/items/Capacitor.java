package app.modules.model.items;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.Values;
import app.modules.model.input.TextInput;

public class Capacitor implements Storable {

    private CapacitorStaff capacitorStaff;

    public double capacitance;
    public double maximumVoltage;

    public Capacitor() {
    }

    @Override
    public Subject toSubject() {
        return Suite.set("capacitance", capacitance).
                set("maximumVoltage", maximumVoltage);
    }

    @Override
    public void fromSubject(Subject subject) {
        if(subject.isi("capacitance", Double.class)) {
            setCapacitance(subject.get("capacitance"));
        }
        if(subject.isi("maximumVoltage", Double.class)) {
            setCapacitance(subject.get("maximumVoltage"));
        }
    }

    public CapacitorStaff getCapacitorStaff() {
        return capacitorStaff;
    }

    public void setCapacitorStaff(CapacitorStaff capacitorStaff) {
        this.capacitorStaff = capacitorStaff;
    }

    public double getCapacitance() {
        return capacitance;
    }

    public void setCapacitance(double capacitance) {
        this.capacitance = capacitance;
    }

    public double getMaximumVoltage() {
        return maximumVoltage;
    }

    public void setMaximumVoltage(double maximumVoltage) {
        this.maximumVoltage = maximumVoltage;
    }

    @Override
    public boolean pathPass(String path) {
        return false;
    }

    @Override
    public boolean searchPass(String filter) {
        return false;
    }

    @Override
    public String getTitle() {
        return "Kondensator " + Values.electronicFormat(capacitance, "F");
    }

    @Override
    public Subject getParams() {
        return capacitorStaff.extract(this);
    }

    @Override
    public void setParams(Subject subject) {
        capacitorStaff.stuff(this, subject);
    }

    public static class CapacitorStaff implements StorableStaff {
        private TextInput capacitanceInput = new TextInput("Pojemność");
        private TextInput voltageInput = new TextInput("Max. napięcie zasilania");

        public Capacitor compose(Subject subject) {
            Capacitor capacitor = new Capacitor();
            stuff(capacitor, subject);
            return capacitor;
        }

        public Subject inputs() {
            return Suite.
                    set(capacitanceInput, "").
                    set(voltageInput, "");
        }

        private Subject extract(Capacitor capacitor) {
            return Suite.
                    set(capacitanceInput, capacitor.getCapacitance()).
                    set(voltageInput, capacitor.getMaximumVoltage());
        }

        private void stuff(Capacitor capacitor, Subject subject) {
            Subject capacitance = capacitanceInput.valueFrom(subject);
            Subject voltage = voltageInput.valueFrom(subject);

            capacitor.fromSubject(Suite.
                    set("capacitance", capacitance.god("value", null)).
                    set("maximumVoltage", voltage.god("value", null)));
        }
    }
}
