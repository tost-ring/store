package app;

import app.core.agent.Aproot;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.transition.Action;
import app.core.suite.transition.HazardousTransition;
import app.core.suite.transition.Transition;
import app.modules.dealer.StoreDealer;
import app.modules.model.Store;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Aproot {

    @Override
    public void employ(Stage primaryStage) {

        HazardousTransition hazardousTransition = (state, in) -> {throw new Exception();};
        Suite.set("", (Transition) (state, in) -> {}).sos("", (Transition) (state, in) -> {});
        Suite.set("", (Action) (state, in) -> Suite.set()).sos("", (Action) (state, in) -> Suite.set());
        Suite.set("", hazardousTransition).sos("", (state, in) -> {throw new Exception();});

        Suite.set("", (in) -> {}).sos("", (in) -> {});
        Suite.set("", (in) -> Suite.set()).sos("", (in) -> Suite.set());
        var s = Suite.set("", (in) -> {throw new Exception();}).sos("", (in) -> {throw new Exception();});

        Suite.set("", () -> {}).sos("", () -> {});
        var s1 = Suite.set("", () -> Suite.set("Here")).sor("", () -> Suite.set());
        Suite.set("", () -> {throw new Exception();}).sos("", () -> {throw new Exception();});

        try {
            var r = s1.ace("");
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        var Ares = subjectA.act("A");
//        System.out.println(Ares.god(0));
//        subjectB.act("B");

//        StoreDealer storeDealer = new StoreDealer();
//        File storeFile = new File("store.jorg");
//        Store store;
//        try {
//            store = storeDealer.loadStore(storeFile);
//        } catch (Exception e) {
//            store = new Store(Suite.
//                    set("Plyty").
//                    set("Elementy", Suite.
//                            set("Kondensator", Suite.
//                                    set("Lokalizacje")).
//                            set("Rezystor")).
//                    set("Lokalizacje"));
//        }
//
//        primaryStage.setTitle("Magazynier");
//        showView(Suite.set(Controller.fxml, "search").
//                set(Window.class, primaryStage).
//                set(Controller.employStuff, Suite.set(Store.class, store)));
//
//        suite.set(Store.class, store).
//                set(File.class, storeFile).
//                set(StoreDealer.class, storeDealer);
    }

    @Override
    public void fire() {
        order("SaveStore");
    }

    @Override
    public Subject fulfil(Subject subject) {
        if(subject.is("SaveStore")) {
            Store store = suite.get(Store.class);
            File file = suite.get(File.class);
            StoreDealer storeDealer = suite.get(StoreDealer.class);
            try {
                storeDealer.saveStore(store, file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return Suite.set();
    }
}
