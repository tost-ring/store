package app.core.agent;

import app.core.suite.Subject;
import javafx.scene.Parent;

public abstract class View extends Controller {
    protected abstract Parent construct(Subject subject);

}
