package app.controller;

import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.Store;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

public class MenuController extends Controller {

    @FXML
    private StackPane stack;

    @FXML
    private FlowPane flow;

    private Store store;

    @Override
    protected Subject employ(Subject subject) {
        store = subject.get(Store.class);
        flow.getChildren().addAll(
                makeOptionButton("Lokalizacje", "lokalizacja-icon.png", "location"),
                makeOptionButton("Plyty", "plyta-icon.png", "board"),
                makeOptionButton("Elementy", "element-icon.png", "element")
        );
        return Suite.set();
    }

    private Parent makeOptionButton(String buttonName, String imageName, String token) {
        Subject option = aproot().loadView(this, Suite
                .set(Controller.class, new MiniatureView()).set(employStuff, Suite
                        .set("buttonName", buttonName)
                        .set("imageName", imageName)
                        .set(tokenString, token)));
        return option.gac(Controller.class).parent();
    }

    @Override
    public Subject fulfil(Subject subject) {
        if(subject.is(tokenString))
            switch (subject.getAs(tokenString, String.class)) {
                case "location":
                    aproot().showView(this, Suite.set(fxml, "locations").set(Window.class, window()).set(employStuff, Suite
                        .set(Store.class, store)));
                    break;
                case "board":
                    aproot().showView(this, Suite.set(fxml, "boards").set(Window.class, window()).set(employStuff, Suite
                            .set(Store.class, store)));
                    break;
                case "element":
                    aproot().showView(this, Suite.set(fxml, "elements").set(Window.class, window()).set(employStuff, Suite
                            .set(Store.class, store)));
                    break;

            }
        return super.fulfil(subject);
    }
}