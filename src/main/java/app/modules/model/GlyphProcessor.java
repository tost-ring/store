package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.processor.IntProcessor;
import app.modules.model.processor.ProcessorException;

import java.util.LinkedList;
import java.util.Queue;

public class GlyphProcessor implements IntProcessor {

    public static String[] process(String string, boolean   standardize) {
        return new GlyphProcessor(standardize).process(string);
    }

    enum State {
        QUOTED, UNQUOTED, PENDING
    }

    private boolean standardize;
    private Queue<String> strings;
    private StringBuilder builder;
    private State state;

    public GlyphProcessor(boolean standardize) {
        this.standardize = standardize;
    }

    public boolean isStandardize() {
        return standardize;
    }

    public void setStandardize(boolean standardize) {
        this.standardize = standardize;
    }

    @Override
    public Subject ready() {
        strings = new LinkedList<>();
        state = State.PENDING;
        return Suite.set();
    }

    @Override
    public void advance(int i) throws ProcessorException {
        switch (state) {
            case PENDING:
                if(i == '"') {
                    state = State.QUOTED;
                    builder = new StringBuilder();
                } else if(!Character.isWhitespace(i)) {
                    state = State.UNQUOTED;
                    builder = new StringBuilder();
                    builder.appendCodePoint(i);
                }
                break;
            case QUOTED:
                if(i == '"') {
                    String str = builder.toString();
                    if(!str.isEmpty()) {
                        strings.add(str);
                        state = State.PENDING;
                    }
                } else {
                    builder.appendCodePoint(standardize ? Character.toLowerCase(i) : i);
                }
                break;
            case UNQUOTED:
                if(i == '"') {
                    String str = builder.toString();
                    if(!str.isEmpty()) {
                        strings.add(str);
                    }
                    state = State.QUOTED;
                } else if(Character.isWhitespace(i)) {
                    String str = builder.toString();
                    if(!str.isEmpty()) {
                        strings.add(str);
                    }
                    state = State.PENDING;
                } else {
                    builder.appendCodePoint(standardize ? Character.toLowerCase(i) : i);
                }
        }
    }

    @Override
    public Subject finish() throws ProcessorException {
        if(state == State.QUOTED || state == State.UNQUOTED) {
            String str = builder.toString();
            if(!str.isEmpty()) {
                strings.add(str);
            }
        }
        return Suite.set(strings);
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
