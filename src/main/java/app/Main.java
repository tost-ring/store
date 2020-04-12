package app;

import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.flow.Chain;
import app.core.jorg.JorgReader;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.modules.graph.Graphs;
import app.modules.graph.ReferenceHashGraph;
import app.modules.model.JorgProcessor;
import app.modules.model.ProcessorException;
import app.modules.model.Store;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;

public class Main extends Aproot {

    private Stage primaryStage;
    private Controller superStore;

    public enum Please {
        showSuperStore
    }

    @Override
    public void employ(Stage primaryStage) {

        this.primaryStage = primaryStage;

        String jorg = "@[a]#java.lang.Object# @[b]#java.lang.Object# @[xkey]#app.core.jorg.Xkey#]@a]@b";
        System.out.println(JorgReader.parse(jorg));

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
