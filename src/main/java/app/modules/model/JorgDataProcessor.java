package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgDataProcessor implements IntProcessor {

    enum State {
        DEFAULT, HUMBLE_STRING, REGULAR_STRING, NUMBER, SOCKET, REFERENCE
    }

    private Subject processors = Suite.set();

    @Override
    public Subject ready() {
        setState(State.DEFAULT);
        return Suite.set();
    }

    @Override
    public Subject finish() throws ProcessorException {
        return processors.getAs(IntProcessor.class).finish();
    }

    private IntProcessor humbleStringProcessor() {
        return new StringComposer() {
            @Override
            public Subject advance(int i) {
                if(Character.isLetterOrDigit(i) || i == '_') {
                    builder.appendCodePoint(i);
                    return Suite.set();
                } else {
                    return Suite.set(ResultType.UNSUPPORTED, Suite.add(i));
                }
            }
        };
    }

    private IntProcessor regularStringProcessor() {
        return new StringComposer() {
            @Override
            public Subject advance(int i) {
                if(i == '"') {
                    return Suite.set(ResultType.RESULT_READY);
                } else {
                    builder.appendCodePoint(i);
                    return Suite.set();
                }
            }
        };
    }

    private IntProcessor defaultProcessor() {
        return i -> {
            if(i == '@') {
                setState(State.REFERENCE);
                return Suite.set();
            } else if(i == '"') {
                setState(State.REGULAR_STRING);
                return Suite.set();
            } else if(i == '#') {
                setState(State.SOCKET);
                return Suite.set();
            } else if(Character.isLetter(i) || i == '_') {
                setState(State.HUMBLE_STRING);
                return JorgDataProcessor.this.advance(i);
            } else if(Character.isDigit(i)) {
                setState(State.NUMBER);
                return JorgDataProcessor.this.advance(i);
            } else if(!Character.isWhitespace(i)) {
                return Suite.set();
            } else {
                return Suite.set(ResultType.UNSUPPORTED, Suite.add(i));
            }
        };
    }

    private void setState(State state) {
        switch (state) {
            case REGULAR_STRING:
                processors.gms(State.REGULAR_STRING, this::regularStringProcessor).ready();
                break;
            case HUMBLE_STRING:
                processors.gms(State.HUMBLE_STRING, this::humbleStringProcessor).ready();
                break;
            case NUMBER:
                processors.gms(State.NUMBER, JorgNumberProcessor::new).ready();
                break;
            case REFERENCE:
                processors.gms(State.REFERENCE, JorgReferenceProcessor::new).ready();
                break;
            case SOCKET:
                processors.gms(State.SOCKET, JorgSocketProcessor::new).ready();
                break;
            default:
                processors.gms(State.DEFAULT, this::defaultProcessor).ready();
        }
    }

    public Subject advance(int i) throws ProcessorException {
        IntProcessor processor = processors.get();
        var advanceResult = processor.advance(i);
        if(advanceResult.is(ResultType.UNSUPPORTED) || advanceResult.is(ResultType.RESULT_READY)) {
            return advanceResult;
        }
        return Suite.set();
    }
}
