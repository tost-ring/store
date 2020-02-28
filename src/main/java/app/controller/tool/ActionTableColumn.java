package app.controller.tool;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.action.Impression;
import javafx.scene.control.TableColumn;

public class ActionTableColumn {

    public static<S, T> TableColumn<S, T> make(String title, Subject buttons, Impression buttonPressed) {
        TableColumn<S, T> actionColumn = new TableColumn<>(title);
        actionColumn.setMaxWidth(Double.MAX_VALUE);
        actionColumn.setEditable(true);
        actionColumn.setCellFactory(ButtonTableCell.forTableColumn(buttons, buttonPressed));
        return actionColumn;
    }
}
