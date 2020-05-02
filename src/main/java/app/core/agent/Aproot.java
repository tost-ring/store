package app.core.agent;

import app.core.NativeString;
import app.core.suite.Subject;
import app.core.suite.Suite;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class Aproot extends Application {

    public enum Please {
        /**
         * Dokumentacja
         */
        loadView,
        showView,
        closeStage}

    private final String fxmlDefaultPrePath = "/fxml/";
    private final String fxmlDefaultPostPath = ".fxml";
    private final String imageDefaultPrePath = "/img/";

    protected Broker secretary = new Broker() {
        @Override
        public Subject fulfil(Subject subject) {
            Broker broker = subject.get(Client.source).asExpected();
            Subject result = Aproot.this.fulfill(broker, subject);
            return result.setAll(Aproot.this.fulfil(subject).front());
        }
    };
    protected Subject suite;
    private Subject dictionary;

    @Override
    public final void init() {
        suite = Suite.set(Aproot.class, this);
    }

    @Override
    public final void start(Stage primaryStage) {
//        JorgReader reader = new JorgReader(new GeneralPerformer(Suite.set("", NativeString.class)));
//        if(reader.readWell(getResource("/jorg/dictionary.jorg"))) {
//            dictionary = reader.getObjects();
//        } else {
//            dictionary = Suite.set();
//        }
//        setNation("pl");
        employ(primaryStage);
    }

    @Override
    public final void stop() throws Exception {
        fire();
        super.stop();
    }

    protected final Subject order(Subject subject) {
        return secretary.order(secretary, subject);
    }
    protected final Subject order(Object object) {
        return secretary.order(secretary, object);
    }

    private Subject fulfill(Broker broker, Subject subject) {
        if(subject.get(Please.loadView).settled()) {
            return loadView(broker, subject);
        }else if(subject.get(Please.showView).settled()) {
            return showView(broker, subject);
        }else if(subject.get(Please.closeStage).settled()) {
            if(broker instanceof Controller) {
                Window window = ((Controller) broker).window();
                if(window instanceof Stage) {
                    ((Stage) window).close();
                }
            }
        } else if(subject.get("Suite").settled()) {
            return suite;
        }
        return Suite.set();
    }

    public abstract void employ(Stage primaryStage);
    public void fire() {}
    public Subject fulfil(Subject subject) {
        return Suite.set();
    }

    public final Subject loadView(Broker broker, Subject subject){
        Controller controller = subject.get(Controller.class).orGiven(null);
        if(controller == null) {
            String fxml = subject.get(Controller.fxml).asExpected();
            URL fxmlUrl = getFxmlResource(fxml);
            if (fxmlUrl == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd aplikacji");
                alert.setHeaderText("Błąd podczas otwierania pliku");
                alert.setContentText("Zasób fxml '" + fxml + "' nie został znaleziony");
                alert.showAndWait();
                throw new ExceptionInInitializerError("Pane load fail");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent parent;
            try {
                parent = loader.load();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd aplikacji");
                alert.setHeaderText("Błąd podczas ładowania widoku");
                alert.setContentText("Sprawdź plik: " + fxmlUrl.getFile());
                alert.showAndWait();
                e.printStackTrace();
                throw new ExceptionInInitializerError("Pane load fail");
            }
            controller = loader.getController();
            if (controller == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd aplikacji");
                alert.setHeaderText("Błąd podczas ładowania kontrolera");
                alert.setContentText("Sprawdź plik: " + fxmlUrl.getFile());
                alert.showAndWait();
                throw new ExceptionInInitializerError("Pane load fail");
            }
            controller.setParent(parent);
        }
        controller.setAproot(this);
        controller.setBoss(broker);
        Subject employ = subject.get(Controller.employStuff);
        if(controller instanceof View) {
            controller.setParent(((View) controller).construct(employ));
        }
        employ = controller.internalEmploy(employ);
        return Suite.set(Controller.class, controller).set(Controller.employStuff, employ);
    }

    protected final Subject loadView(Subject subject){
        return loadView(secretary, subject);
    }

    public final Subject showView(Broker broker, Subject subject) {
        Controller controller = subject.get(Controller.class).orGiven(null);
        if(controller == null || controller.parent() == null) {
            subject.setAll(loadView(broker, subject).front());
            controller = subject.get(Controller.class).asExpected();
        }

        Scene scene = subject.get(Scene.class).orDo(controller::scene);
        if(scene == null) scene = new Scene(controller.parent());
        else scene.setRoot(controller.parent());
        Stage stage;
        Window window = subject.get(Window.class).orDo(controller::window);
        if(window == null) {
            stage = new Stage();
            stage.setScene(scene);
            if(subject.get("StageTitle").settled()) {
                stage.setTitle(subject.get("StageTitle").asExpected());
            }
        } else if(window instanceof Stage) {
            stage = (Stage)window;
            stage.setScene(scene);
            if(subject.get("StageTitle").settled()) {
                stage.setTitle(subject.get("StageTitle").asExpected());
            }
        } else {
            throw new ClassCastException("Cannot show " + window);
        }
        Subject dress = controller.internalDress(subject.get(Controller.dressStuff).set(Scene.class, scene).set(Stage.class, stage));
        stage.show();
        return subject.set(Controller.dressStuff, dress);
    }

    protected final Subject showView(Subject subject){
        return showView(secretary, subject);
    }

    public String getFxmlPrePath() {
        return fxmlDefaultPrePath;
    }

    public String getFxmlPostPath() {
        return fxmlDefaultPostPath;
    }

    public String getImagePrePath() {
        return imageDefaultPrePath;
    }

    public final URL getResource(String forPath){
        return getClass().getResource(forPath);
    }

    public final URL getFxmlResource(String fxml){return getResource(getFxmlPrePath() + fxml + getFxmlPostPath());}

    public final URL getImageResource(String image){return getResource(getImagePrePath() + image);}

    public void setNation(String nation) {
        for(var nativeString : dictionary.front().values().filter(NativeString.class)) {
            nativeString.setNation(nation);
        }
    }

    public NativeString getNativeString(String id) {
        return dictionary.get(id).orGiven(null);
    }

    public String getString(String id) {
        return dictionary.get(id).direct().toString();
    }
}
