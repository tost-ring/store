package app.modules.model;

import app.core.jorg.Xkey;
import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgNumberProcessor implements IntProcessor {

    private StringBuilder builder;
    private boolean isDouble;

    @Override
    public Subject ready() {
        builder = new StringBuilder();
        isDouble = false;
        return Suite.set();
    }

    @Override
    public Subject advance(int i) {
        if(Character.isDigit(i)) {
            builder.appendCodePoint(i);
        } else if(i == '.' || i == ',') {
            if(isDouble) {
                return Suite.set(ResultType.UNSUPPORTED, Suite.add(i));
            } else {
                builder.append('.');
                isDouble = true;
            }
        } else {
            return Suite.set(ResultType.UNSUPPORTED, Suite.add(i));
        }
        return Suite.set();
    }

    @Override
    public Subject finish() throws ProcessorException {
        try {
            if (isDouble) {
                return Suite.set(ResultType.RESULT, Double.parseDouble(builder.toString()));
            } else {
                return Suite.set(ResultType.RESULT, Integer.parseInt(builder.toString()));
            }
        } catch(Exception e) {
            throw new ProcessorException("Imparsable number given");
        }
    }
}
