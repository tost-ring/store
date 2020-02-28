package app.modules.model;

import app.core.flow.FlowArrayList;
import app.core.suite.Subject;
import app.core.suite.Subjective;

public class Store implements Subjective {

    private FlowArrayList<String> columns;
    private FlowArrayList<Subject> stored = new FlowArrayList<>();

    public Store() {
        columns = new FlowArrayList<>();
    }

    public Store(String ... columns) {
        this.columns = new FlowArrayList<>(columns);
    }

    public FlowArrayList<Subject> getStored() {
        return stored;
    }

    public FlowArrayList<String> getColumns() {
        return columns;
    }
}
