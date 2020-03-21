package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

public abstract class StringComposer implements IntProcessor {

    protected StringBuilder builder;

    @Override
    public Subject ready() {
        builder = new StringBuilder();
        return Suite.set();
    }

    @Override
    public Subject finish() {
        if(builder.length() > 0) {
            return Suite.set(builder.toString());
        } else {
            return Suite.set();
        }
    }
}
