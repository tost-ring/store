package app.modules.model.filter;

import app.core.suite.Subject;
import javafx.beans.Observable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Filter {

    private String field;
    private TitledPane titledPane;
    private Label valueLabel;
    private TextField textField;

    public Filter(String field) {
        this.field = field;
        titledPane = new TitledPane();
        titledPane.setText(field + ":");
        titledPane.setContentDisplay(ContentDisplay.RIGHT);
        textField = new TextField("");
        titledPane.setContent(textField);
        valueLabel = new Label();
        valueLabel.textProperty().bind(textField.textProperty());
        valueLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
        titledPane.setGraphic(valueLabel);
    }

    public TitledPane getTitledPane() {
        return titledPane;
    }

    public Observable getObservable() {
        return textField.textProperty();
    }

    public String getField() {
        return field;
    }

    public boolean pass(Subject subject) {
        String filteringText = textField.getText();
        String data = subject.god(field, null);
        if(data == null) {
            return filteringText.isEmpty();
        }
        return data.contains(filteringText);
    }
}
