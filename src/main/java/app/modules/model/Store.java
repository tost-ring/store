package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Subjective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Store implements Subjective {

    private List<String> columns;
    private List<Subject> stored = new ArrayList<>();

    public Store() {
        columns = new ArrayList<>();
    }

    public Store(String ... columns) {
        this.columns = Arrays.asList(columns);
    }

    public List<Subject> getStored() {
        return stored;
    }

    public List<String> getColumns() {
        return columns;
    }
}
