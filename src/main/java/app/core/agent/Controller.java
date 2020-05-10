package app.core.agent;

import app.core.suite.Subject;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public abstract class Controller extends Agent {

    public static final Object employStuff = new Object();
    public static final Object dressStuff = new Object();
    public static final Object fxml = new Object();
    public static final Object tokenString = new Object();

    private Aproot aproot;
    private final ChangeListener<Scene> undressCallback = (observableValue, scene, t1) -> internalUndress();
    private final EventHandler<WindowEvent> windowCloseCallback = event -> internalUndress();
    protected Parent parent;
    protected Stage stage;
    protected Scene scene;
    protected Subject suite;


    protected final Subject internalEmploy(Subject subject) {
        suite = subject;
        return employ(subject);
    }

    protected Subject employ(Subject subject) {
        employ();
        return suite;
    }

    protected void employ() {}

    protected final Subject internalDress(Subject subject) {
        stage = subject.get(Stage.class).asExpected();
        scene = subject.get(Scene.class).asExpected();
        parent.sceneProperty().addListener(undressCallback);
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, windowCloseCallback);
        suite = subject;
        return dress(subject);
    }

    protected Subject dress(Subject subject){
        dress();
        return suite;
    }

    protected void dress(){}

    protected final void internalUndress() {
        parent.sceneProperty().removeListener(undressCallback);
        stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, windowCloseCallback);
        undress();
    }

    protected void undress(){}

    protected final Aproot aproot() {
        return aproot;
    }

    protected final void setAproot(Aproot aproot) {
        this.aproot = aproot;
    }

    protected final Window window() {
        Scene scene = scene();
        return scene == null ? null : scene.getWindow();
    }

    protected final Stage stage() {
        return stage;
    }

    protected final Scene scene() {
        return scene;
    }

    public final Parent parent() {
        return parent;
    }

    protected final void setParent(Parent parent) {
        this.parent = parent;
    }
}
