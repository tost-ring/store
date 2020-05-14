package app.modules.model;

import app.core.jorg.Performable;
import app.core.jorg.Reformable;
import app.core.jorg.StandardReformer;
import app.core.jorg.util.PortableList;
import app.core.suite.Subject;

import java.util.ArrayList;
import java.util.List;

public class Store implements Performable, Reformable {

    private final  List<String> columns;
    private final  List<Subject> stored = new ArrayList<>();
    private boolean advancedMode = false;

    public Store() {
        columns = new ArrayList<>();
    }

    public Store(String ... columns) {
        this.columns = PortableList.of(columns);
    }

    public List<Subject> getStored() {
        return stored;
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean isAdvancedMode() {
        return advancedMode;
    }

    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

}
