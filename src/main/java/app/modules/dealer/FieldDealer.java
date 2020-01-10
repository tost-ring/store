package app.modules.dealer;

import app.core.suite.Subject;
import app.modules.model.filter.*;
import app.modules.model.input.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;


public class FieldDealer {

    public Filter createFilter(String field) {
        return new Filter(field);
    }

    public TableColumn<Subject, Object> createColumn(String field) {
        TableColumn<Subject, Object> column = new TableColumn<>(field);
        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().god(field, "")));

        return column;
    }

    public Input createInput(String field, Object initialValue) {
        if(initialValue != null) {
            return new Input(field, initialValue.toString());
        } else {
            return new Input(field, "");
        }
    }
}
