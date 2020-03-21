package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgReferenceProcessor implements IntProcessor {

    private StringBuilder builder;

    @Override
    public Subject ready() {
        builder = new StringBuilder();
        return Suite.set();
    }

    @Override
    public Subject advance(int i) {
        if(Character.isLetterOrDigit(i) || i == '_') {
            builder.appendCodePoint(i);
        } else {
            return Suite.set(ResultType.UNSUPPORTED, Suite.add(i));
        }
        return Suite.set();
    }

    @Override
    public Subject finish() throws ProcessorException {
        if(builder.length() > 0) {
            return Suite.set(ResultType.RESULT, new Reference(builder.toString()));
        } else {
            throw new ProcessorException("Empty reference");
        }
    }
}
