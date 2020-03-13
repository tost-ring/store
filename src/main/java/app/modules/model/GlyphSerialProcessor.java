package app.modules.model;

import app.core.flow.FlowIterator;
import app.core.suite.Sub;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GlyphSerialProcessor implements IntSerialProcessor {

    public static String[] process(String string, boolean standardize) {
        return new GlyphSerialProcessor(standardize).process(string);
    }

    enum State {
        QUOTED, UNQUOTED
    }

    private Subject processors = Suite.set();

    private boolean standardize;
    private State state;
    private Queue<String> strings;

    public GlyphSerialProcessor(boolean standardize) {
        this.standardize = standardize;
    }

    private IntSerialProcessor quotedProcessor() {
        return new IntSerialProcessor() {
            StringBuilder builder;

            @Override
            public Subject ready() {
                builder = new StringBuilder();
                return Suite.set();
            }

            @Override
            public Subject advance(int codePoint) {
                if(codePoint == '"') {
                    return Suite.error("Quote mark");
                } else {
                    builder.appendCodePoint(standardize ? Character.toLowerCase(codePoint) : codePoint);
                    return Suite.set();
                }
            }

            @Override
            public Subject finish() {
                return builder.length() > 0 ? Suite.set(builder.toString()) : Suite.set();
            }
        };
    }

    private IntSerialProcessor unquotedProcessor() {
        return new IntSerialProcessor() {
            StringBuilder builder = new StringBuilder();

            @Override
            public Subject ready() {
                builder = new StringBuilder();
                return Suite.set();
            }

            @Override
            public Subject advance(int codePoint) {
                if(Character.isWhitespace(codePoint)) {
                    return Suite.error("Whitespace mark");
                } else if(codePoint == '"') {
                    return Suite.error("Quote mark");
                } else {
                    builder.appendCodePoint(standardize ? Character.toLowerCase(codePoint) : codePoint);
                    return Suite.set();
                }
            }

            @Override
            public Subject finish() {
                return builder.length() > 0 ? Suite.set(builder.toString()) : Suite.set();
            }
        };
    }

    private boolean setState(State state) {
        this.state = state;
        switch (state) {
            case QUOTED:
                processors.gms(state, this::quotedProcessor).ready();
                return true;
            case UNQUOTED:
                processors.gms(state, this::unquotedProcessor).ready();
                return true;
            default:
                return false;
        }
    }

    @Override
    public Subject ready() {
        strings = new LinkedList<>();
        setState(State.UNQUOTED);
        return Suite.set();
    }

    @Override
    public Subject advance(int codePoint) {
//        switch (state) {
//            case QUOTED:
//                sub = processors.getAs(IntSerialProcessor.class).advance(codePoint);
//                if(sub.is()) {
//                    sub = quotedProcessor.finish();
//                    if(sub.is()) {
//                        strings.add(sub.getAsExpected());
//                    }
//                    setState(State.UNQUOTED);
//                }
//                return Suite.set();
//            case UNQUOTED:
//                sub = unquotedProcessor.advance(codePoint);
//                if(sub.is("closed")) {
//                    String str = sub.get("data");
//                    if(!str.isEmpty()) {
//                        strings.add(sub.get("data"));
//                    }
//                    unquotedProcessor.ready();
//                    state = State.UNQUOTED;
//                }
//                if(!sub.is("consumed") && codePoint == '"') {
//                    quotedProcessor.ready();
//                    state = State.QUOTED;
//                }
//                return Suite.mix(sub, "consumed");
//            default:
//                return Suite.set();
//        }
        return Suite.set();
    }

    @Override
    public Subject finish() {
//        Subject sub;
//        switch (state) {
//            case QUOTED:
//                sub = quotedProcessor.finish();
//                if(sub.is("closed")) {
//                    String str = sub.get("data");
//                    if(!str.isEmpty()) {
//                        strings.add(sub.get("data"));
//                    }
//                }
//                break;
//            case UNQUOTED:
//                sub = unquotedProcessor.finish();
//                if(sub.is("closed")) {
//                    String str = sub.get("data");
//                    if(!str.isEmpty()) {
//                        strings.add(sub.get("data"));
//                    }
//                }
//                break;
//        }
//        return Suite.set("closed").set("data", strings);
        return Suite.set();
    }

    public String[] process(String str) {
        ready();
        str.chars().forEach(this::advance);
        finish();
        return strings.toArray(new String[0]);
    }
}
