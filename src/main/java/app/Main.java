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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Aproot {

    private Stage primaryStage;
    private Controller superStore;

    public enum Please {
        showSuperStore
    }

    @Override
    public void employ(Stage primaryStage) {

        this.primaryStage = primaryStage;

//        System.out.println(JorgWriter.encode(new File("filet")));
        List<String> list = new ArrayList<>();
        list.add("str");
        list.add("123");
        JorgWriter writer = new JorgWriter();
        writer.getPerformer().addPort(ArrayList.class, "List");
        writer.getPerformer().addPort("str", "ported");
        writer.addObject("obj", list);
        writer.addObject("fil", new File("filet"));
        writer.saveWell(new File("nowy.jorg"));


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
