package app.modules.model;

import app.core.jorg.Performable;
import app.core.jorg.Reformable;
import app.core.jorg.util.MoldList;
import app.core.suite.Subject;

import java.util.ArrayList;
import java.util.List;

public class Store implements Performable, Reformable {

    private final List<String> columns;
    private final List<Subject> stored = new ArrayList<>();

    public Store() {
        columns = new ArrayList<>();
    }

    public Store(String ... columns) {
        this.columns = MoldList.of(columns);
    }

    public List<Subject> getStored() {
        return stored;
    }

    public List<String> getColumns() {
        return columns;
    }

}
