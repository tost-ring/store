package app.core.agent;

import app.core.suite.Subject;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class Controller extends Agent {

    public static final Object employStuff = new Object();
    public static final Object dressStuff = new Object();
    public static final Object fxml = new Object();
    public static final Object tokenString = new Object();

    private Aproot aproot;
    private ChangeListener<Scene> sceneChangeCallback = (observableValue, scene, t1) -> {
        if(scene == scene()) {
            undress();
        }
    };
    protected Parent parent;
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
        window().setOnHiding(event -> undress());
        stage().sceneProperty().addListener(sceneChangeCallback);
        suite = subject;
        return dress(subject);
    }

    protected Subject dress(Subject subject){
        dress();
        return suite;
    }

    protected void dress(){}

    protected final void internalUndress() {
        stage().sceneProperty().removeListener(sceneChangeCallback);
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
        Window window = window();
        return window instanceof Stage ? (Stage)window : null;
    }

    protected final Scene scene() {
        return parent == null ? null : parent.getScene();
    }

    public final Parent parent() {
        return parent;
    }

    protected final void setParent(Parent parent) {
        this.parent = parent;
    }
}
