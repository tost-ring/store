package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.LinkedList;
import java.util.Queue;

public class GlyphProcessor implements IntProcessor {

    public static String[] process(String string, boolean standardize) {
        return new GlyphProcessor(standardize).process(string);
    }

    enum State {
        QUOTED, UNQUOTED, DEFAULT
    }

    private Subject processors = Suite.set();

    private boolean standardize;
    private Queue<String> strings;

    public GlyphProcessor(boolean standardize) {
        this.standardize = standardize;
    }

    private IntProcessor quotedProcessor() {
        return new StringComposer() {

            @Override
            public Subject advance(int codePoint) {
                if(codePoint == '"') {
                    return Suite.set(ResultType.UNSUPPORTED, "Quote mark");
                } else {
                    builder.appendCodePoint(standardize ? Character.toLowerCase(codePoint) : codePoint);
                    return Suite.set();
                }
            }

            @Override
            public Subject finish() {
                Subject sub = super.finish();
                if(sub.settled()) {
                    strings.add(sub.asExpected());
                }
                return Suite.set();
            }
        };
    }

    private IntProcessor unquotedProcessor() {
        return new StringComposer() {

            @Override
            public Subject advance(int codePoint) {
                if(Character.isWhitespace(codePoint)) {
                    return Suite.set(ResultType.UNSUPPORTED, "Whitespace mark");
                } else if(codePoint == '"') {
                    return Suite.set(ResultType.UNSUPPORTED, "Quote mark");
                } else {
                    builder.appendCodePoint(standardize ? Character.toLowerCase(codePoint) : codePoint);
                    return Suite.set();
                }
            }

            @Override
            public Subject finish() {
                Subject sub = super.finish();
                if(sub.settled()) {
                    strings.add(sub.asExpected());
                }
                return Suite.set();
            }
        };
    }

    private IntProcessor defaultProcessor() {
        return i -> {
            if(i == '"') {
                setState(State.QUOTED);
            } else if(!Character.isWhitespace(i)) {
                setState(State.UNQUOTED);
                return GlyphProcessor.this.advance(i);
            }
            return Suite.set();
        };
    }

    private void setState(State state) {
        switch (state) {
            case QUOTED:
                processors.getDone(state, this::quotedProcessor).asGiven(IntProcessor.class).ready();
                return;
            case UNQUOTED:
                processors.getDone(state, this::unquotedProcessor).asGiven(IntProcessor.class).ready();
                return;
            default:
                processors.getDone(state, this::defaultProcessor).asGiven(IntProcessor.class).ready();
        }
    }

    @Override
    public Subject ready() {
        strings = new LinkedList<>();
        setState(State.DEFAULT);
        return Suite.set();
    }

    @Override
    public Subject advance(int codePoint) throws ProcessorException {
        IntProcessor processor = processors.asExpected();
        if(processor.advance(codePoint).settled()) {
            processor.finish();
            setState(State.DEFAULT);
        }
        return Suite.set();
    }

    @Override
    public Subject finish() throws ProcessorException {
        processors.asGiven(IntProcessor.class).finish();
        return Suite.set(ResultType.RESULT, strings);
    }

    public String[] process(String str) {
        try {
            ready();
            for(int i : str.chars().toArray()) {
                advance(i);
            }
            finish();
            return strings.toArray(new String[0]);
        } catch (ProcessorException pe) {
            return new String[0];
        }
    }
}
