package app.controller;

import app.core.agent.Controller;
import app.core.suite.*;
import app.core.flow.FlowCollection;
import app.modules.dealer.FieldDealer;
import app.modules.model.input.Input;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Record extends Controller {

    @FXML
    private StackPane stack;

    @FXML
    private VBox box;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private FieldDealer fieldDealer;
    private FlowCollection<Input> inputs;

    private Subject subject;
    private Object response;

    /*@Override
    protected Subject dress(Subject subject) {
        fieldDealer = subject.gon(FieldDealer.class);
        this.subject = subject.get(Subject.class);
        response = subject.get(token);
        inputs = subject.get("fields", Glass.flowCollection(String.class)).mapTo(subjectField ->
                fieldDealer.createInput(subjectField, this.subject.god(subjectField, null)), true);

        box.getChildren().clear();
        double prefHeight = 200, prefWidth = 150;
        for(Input input : inputs) {
            Node node = input.getMainNode();
            box.getChildren().add(node);
            prefHeight += input.getPrefHeight();
            prefWidth = Math.max(prefWidth, input.getPrefWidth());
        }
        prefHeight = Math.min(prefHeight, Screen.getPrimary().getBounds().getHeight());
        prefWidth = Math.min(prefWidth + 10, Screen.getPrimary().getBounds().getWidth());
        box.getChildren().setAll(inputs.mapTo(Input::getMainNode));
        stack.setPrefSize(prefWidth, prefHeight);

        return Sub.voidSubject();
    }

    @Override
    public Subject fulfil(Client from, Subject subject) {
        return order(subject);
    }

    @FXML
    void cancelAction(ActionEvent event) {
        order(Sub.set(closeStage));
    }

    @FXML
    void saveAction(ActionEvent event) {
        for(Input input : inputs) {
            subject.set(input.getField(), input.getValue());
        }
        order(Sub.set(token, response).set(Subject.class, subject));
        order(Sub.set(closeStage));
    }*/
}
