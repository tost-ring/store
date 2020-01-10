package app.core.agent;

import app.core.suite.Coupon;
import app.core.suite.Subject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class Controller extends Agent {

    public static final Coupon<Subject> employStuff = Coupon.forObjectOf(Subject.class);
    public static final Coupon<Subject> dressStuff = Coupon.forObjectOf(Subject.class);
    public static final Object fxml = new Object();
    public static final Coupon<String> tokenString = Coupon.forObjectOf(String.class);

    private Aproot aproot;
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
        suite = subject;
        return dress(subject);
    }

    protected Subject dress(Subject subject){
        dress();
        return suite;
    }

    protected void dress(){}

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
