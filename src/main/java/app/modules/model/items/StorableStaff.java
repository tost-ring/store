package app.modules.model.items;

import app.core.suite.Subject;
import app.core.suite.Subjective;

public interface StorableStaff extends Subjective {
    Subject inputs();
    Storable compose(Subject subject);
}
