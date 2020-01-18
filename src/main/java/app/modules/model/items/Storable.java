package app.modules.model.items;

import app.core.suite.Subject;

public interface Storable {
    boolean pathPass(String path);
    boolean searchPass(String filter);
    String getTitle();
    Subject getMainParams();
    Subject getFullParams();

}
