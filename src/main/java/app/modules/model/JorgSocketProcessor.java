package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgSocketProcessor implements IntProcessor {

    private StringBuilder builder;

    @Override
    public Subject ready() {
        builder = new StringBuilder();
        return Suite.set();
    }

    @Override
    public Subject advance(int i) {
        if(i == '#') {
            return Suite.set(ResultType.RESULT_READY);
        } else {
            builder.appendCodePoint(i);
            return Suite.set();
        }
    }

    @Override
    public Subject finish() {
        return Suite.set(ResultType.RESULT, new Socket(builder.toString()));
    }
}
