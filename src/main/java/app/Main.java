package app;

import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.modules.model.Store;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;

public class Main extends Aproot {

    @Override
    public void employ(Stage primaryStage) {

        StoreDealer storeDealer = new StoreDealer();
        File storeFile = new File("store.jorg");
        Store store;
        try {
            store = storeDealer.loadStore(storeFile);
        } catch (Exception e) {
            store = new Store(Suite.
                    set("Plyty").
                    set("Elementy", Suite.
                            set("Kondensator", Suite.
                                    set("Lokalizacje")).
                            set("Rezystor")).
                    set("Lokalizacje"));
        }

        primaryStage.setTitle("Magazynier");
        showView(Suite.set(Controller.fxml, "search").
                set(Window.class, primaryStage).
                set(Controller.employStuff, Suite.set(Store.class, store)));

        suite.set(Store.class, store).
                set(File.class, storeFile).
                set(StoreDealer.class, storeDealer);
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
