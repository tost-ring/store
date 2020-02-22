package app.modules.model.items;

import app.core.suite.Subject;
import app.core.suite.Subjective;

public interface Storable extends Subjective {
    boolean pathPass(String path);
    boolean searchPass(String filter);
    String getTitle();
    Subject getParams();
    void setParams(Subject subject);
}
