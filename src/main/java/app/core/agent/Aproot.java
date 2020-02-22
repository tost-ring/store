package app.core.agent;

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
            Broker broker = subject.get(Client.source);
            Subject result = Aproot.this.fulfill(broker, subject);
            return result.met(Aproot.this.fulfil(subject));
        }
    };
    protected Subject suite;

    @Override
    public final void init() {
        suite = Suite.set(Aproot.class, this);
    }

    @Override
    public final void start(Stage primaryStage) {
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
        if(subject.is(Please.loadView)) {
            return loadView(broker, subject);
        }else if(subject.is(Please.showView)) {
            return showView(broker, subject);
        }else if(subject.is(Please.closeStage)) {
            if(broker instanceof Controller) {
                Window window = ((Controller) broker).window();
                if(window instanceof Stage) {
                    ((Stage) window).close();
                }
            }
        } else if(subject.is("Suite")) {
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
        Controller controller = subject.god(Controller.class, null);
        if(controller == null) {
            String fxml = subject.get(Controller.fxml);
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
        Subject employ = subject.god(Controller.employStuff, Suite.set());
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
        if(!subject.is(Controller.class) || subject.gac(Controller.class).parent() == null) {
            subject.met(loadView(broker, subject));
        }
        Controller controller = subject.get(Controller.class);
        Scene scene = subject.god(Scene.class, controller.scene());
        if(scene == null) scene = new Scene(controller.parent());
        else scene.setRoot(subject.get(Parent.class));
        Stage stage;
        Window window = subject.god(Window.class, controller.window());
        if(window == null) {
            stage = new Stage();
            stage.setScene(scene);
            if(subject.is("StageTitle")) {
                stage.setTitle(subject.get("StageTitle"));
            }
        } else if(window instanceof Stage) {
            stage = (Stage)window;
            stage.setScene(scene);
            if(subject.is("StageTitle")) {
                stage.setTitle(subject.get("StageTitle"));
            }
        } else {
            throw new ClassCastException("Cannot show " + window);
        }
        Subject dress = controller.internalDress(subject.god(Controller.dressStuff, Suite.set())
                .set(Scene.class, scene).set(Stage.class, stage));
        stage.show();
        return subject.set(Controller.dressStuff, dress);
    }

    protected final Subject showView(Subject subject){
        return showView(secretary, subject);
    }

    public final URL getResource(String forPath){
        return getClass().getResource(forPath);
    }

    public final URL getFxmlResource(String fxml){return getResource(fxmlDefaultPrePath + fxml + fxmlDefaultPostPath);}

    public final URL getImageResource(String image){return getResource(imageDefaultPrePath + image);}
}
