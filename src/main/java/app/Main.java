package app;

import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.jorg.*;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.modules.model.Item;
import app.modules.model.Store;
import app.modules.model.items.Family;
import app.modules.model.items.Person;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Aproot {

    @Override
    public void employ(Stage primaryStage) {

        GeneralPerformer performer = new GeneralPerformer(Suite.set());
        JorgWriter writer = new JorgWriter(performer);
        JorgReader reader = new JorgReader(performer);

        Family pomietlo = new Family(new Person("Celina", "Pomietlo"), new Person("Janusz", "Pomietlo"));
        Person lukasz = new Person("Lukasz", "Pomietlo");
        Person asia = new Person("Asia", "Pomietlo");
        Person gosia = new Person("Gosia", "Pomietlo");
        pomietlo.getChildren().add(lukasz);
        pomietlo.getChildren().add(asia);
        pomietlo.getChildren().add(gosia);

        writer.addObject(pomietlo, "pomietlo");
        try {
            writer.write(new FileOutputStream("store.store"));
            reader.read(new FileInputStream("store.store"));
            System.out.println(reader.getObjects().get("pomietlo", Family.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StoreDealer storeDealer = new StoreDealer();
        File storeFile = new File("magazyn.store");
        Store store;
        Item mainItem;
        try {
            store = storeDealer.loadStore(storeFile);
        } catch (Exception e) {
            store = new Store();
        }
        mainItem = store.find(item -> item.getParent() == null);
        if(mainItem == null) {
            mainItem = new Item("Magazyn", null);
            store.add(mainItem);
        }

        primaryStage.setTitle("Magazynier");
        showView(Suite.set(Controller.fxml, "welcome")
                .set(Window.class, primaryStage)
                .set(Controller.employStuff, Suite.set(Store.class, store).set(Item.class, mainItem)));

        suite.set(Store.class, store)
                .set(File.class, storeFile)
                .set(StoreDealer.class, storeDealer);
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
            storeDealer.saveStore(store, file);
        }

        return Suite.set();
    }
}
