package app;

import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.jorg.Fusible;
import app.core.jorg.JorgPerformer;
import app.core.jorg.JorgWriteException;
import app.core.jorg.JorgWriter;
import app.core.suite.Subject;
import app.core.suite.Suite;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Aproot {

    private Stage primaryStage;
    private Controller superStore;

    public enum Please {
        showSuperStore
    }

    @Override
    public void employ(Stage primaryStage) {

        this.primaryStage = primaryStage;

        Fusible f = () -> Suite.set("/XD");
        System.out.println(JorgWriter.encode((Fusible) () -> Suite.set(1, f).set("255", 255)));


//        order(Please.showSuperStore);
    }

    @Override
    public Subject fulfil(Subject subject) {
        if(subject.get(Please.showSuperStore).settled()) {
            Subject showResult = showView(Suite.set(Controller.fxml, "super-store").
                    set(Window.class, subject.get(Window.class).orGiven(primaryStage)).
                    set(Scene.class, subject.get(Scene.class).orDo(primaryStage::getScene)).
                    set(Controller.class, superStore).
                    set("StageTitle", "Magazynier - wybierz magazyn"));
            superStore = showResult.get(Controller.class).asExpected();
        }
        return Suite.ok();
    }
}
