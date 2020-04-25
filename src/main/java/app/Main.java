package app;

import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.jorg.JorgReader;
import app.core.suite.Subject;
import app.core.suite.Suite;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Main extends Aproot {

    private Stage primaryStage;
    private Controller superStore;

    public enum Please {
        showSuperStore
    }

    @Override
    public void employ(Stage primaryStage) {

        this.primaryStage = primaryStage;

        String jorg = "#[7]java.lang.String   ]a   [2]b   [4]A tutaj moze cos dluzszego   ]c" +
                "@[druga tablica] #[4]java.lang.Integer ]4]3]2]10000" +
                "@[obiekt]  [parametr]wartosc   [ilosc]20";
        var parsed = JorgReader.parse(jorg);
        System.out.println(Arrays.toString(parsed.orGiven(new String[0])));
        System.out.println(Arrays.toString(parsed.get("druga tablica").orGiven(new Integer[0])));
        System.out.println("\nObiekt:\n" + parsed.get("obiekt").direct());


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
