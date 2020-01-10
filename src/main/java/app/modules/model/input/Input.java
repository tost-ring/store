package app.modules.model.input;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class Input {

    private String field;
    private BorderPane borderPane;
    private Label label;
    private TextField textField;

    public Input(String field, String initialText) {
        this.field = field;
        this.borderPane = new BorderPane();
        this.label = new Label(field);
        this.textField = new TextField(initialText);

        borderPane.setTop(this.label);
        borderPane.setCenter(this.textField);
    }

    public String getField() {
        return field;
    }

    public Node getMainNode() {
        return borderPane;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }

    public void setValue(Object value) {
        textField.setText(value.toString());
    }

    public Object getValue() {
        return textField.getText();
    }

    public double getPrefHeight() {
        return label.getPrefHeight() + textField.getPrefHeight();
    }

    public double getPrefWidth() {
        return Math.max(label.getPrefWidth(), textField.getPrefWidth());
    }
}
