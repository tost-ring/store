package app.controller.tool;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.action.Impression;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ButtonTableCell<S, T> extends TableCell<S, T> {
    private HBox buttons;

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(Subject sub, Impression impression) {
        return (var2) -> new ButtonTableCell<>(sub, impression);
    }

    public ButtonTableCell(Subject sub, Impression impression) {
        this.getStyleClass().add("button-table-cell");
        this.buttons = createButtons(this, sub, impression);
    }

    public void updateItem(T var1, boolean var2) {
        super.updateItem(var1, var2);
        if (var2) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            this.setGraphic(this.buttons);
            this.buttons.disableProperty().bind(Bindings.not(this.getTableView().editableProperty().and(this.getTableColumn().editableProperty()).and(this.editableProperty())));
        }
    }

    private static<S, T> HBox createButtons(TableCell<S, T> cell, Subject sub, Impression impression) {
        HBox hbox = new HBox();
        hbox.setMaxWidth(1.7976931348623157E308D);
        hbox.setSpacing(9.0);
        hbox.setAlignment(Pos.CENTER);
        for(Subject it : sub.front()) {
            Button button = new Button(it.asExpected());
            button.setMaxWidth(1.7976931348623157E308D);
            button.setOnAction(event -> {
                impression.revel(Suite.set("value", it.key().direct()).set("row", cell.getIndex()));
            });
            hbox.getChildren().add(button);
        }
        return hbox;
    }
}

