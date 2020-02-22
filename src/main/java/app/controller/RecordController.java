package app.controller;

import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.suite.*;
import app.core.flow.FlowCollection;
import app.modules.dealer.StorableDealer;
import app.modules.model.Store;
import app.modules.model.items.Storable;
import app.modules.model.items.StorableStaff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.lang.reflect.InvocationTargetException;

public class RecordController extends Controller {

    @FXML
    private StackPane stack;

    @FXML
    private VBox box;

    @FXML
    private ComboBox<StorableStaff> element;

    private StorableDealer storableDealer;
    private Storable storable;
    private FlowCollection<InputView> inputViews;

    private Object response;

    @Override
    protected Subject employ(Subject subject) {

        element.valueProperty().addListener((observable, oldValue, newValue) -> {
            storable = null;
            if(newValue != null) {
                try {
                    storable = newValue.compose(Suite.set());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resetBox();
        });

        return Suite.set();
    }

    @Override
    protected Subject dress(Subject subject) {
        response = subject.get(tokenString);
        storableDealer = subject.gon(StorableDealer.class);
        Store store = subject.get(Store.class);
        element.getItems().setAll(store.getElementsTypes());
        if(subject.is(Class.class)) {
            element.setValue(subject.get(Class.class));
        }

        return Suite.set();
    }

    @Override
    public Subject fulfil(Subject subject) {
        return order(subject);
    }

    private void resetBox() {
        box.getChildren().clear();
        if(storable != null) {
            inputViews = storableDealer.createInputViews(storable);
            double prefHeight = 200, prefWidth = 150;
            for (InputView inputView : inputViews) {
                Node node = inputView.getMainNode();
                box.getChildren().add(node);
                prefHeight += inputView.getPrefHeight();
                prefWidth = Math.max(prefWidth, inputView.getPrefWidth());
            }
            prefHeight = Math.min(prefHeight, Screen.getPrimary().getBounds().getHeight());
            prefWidth = Math.min(prefWidth + 10, Screen.getPrimary().getBounds().getWidth());
            box.getChildren().setAll(inputViews.mapTo(InputView::getMainNode));
            stack.setPrefSize(prefWidth, prefHeight);
        }
    }

    @FXML
    void cancelAction(ActionEvent event) {
        order(Aproot.Please.closeStage);
    }

    @FXML
    void saveAction(ActionEvent event) {
        storableDealer.pollInputViews(inputViews, storable);
        order(Suite.set(tokenString, response).set(Storable.class, storable));
        order(Aproot.Please.closeStage);
    }
}
