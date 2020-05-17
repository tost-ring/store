package app.core.jorg;

import app.core.processor.IntProcessor;
import app.core.processor.ProcessorException;
import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgProcessor implements IntProcessor {

    private static final Xkey nullXkey = new Xkey(null, null, true);
    private static final Xkey terminatorXkey = new Xkey(Jorg.terminator, null, true);

    enum State {
        AT, FROM_NODE, DIRECT, AFTER_DIRECT, VIA_NODE,
        AFTER_VIA_NODE, TO_NODE, AFTER_TO_NODE
    }

    enum DataState {
        PENDING, HUMBLE_STRING, STRING, REFERENCE, NUMBER, REAL_NUMBER, MINUS, PLUS
    }

    private Subject keys;
    private State state;
    private DataState dataState;
    private StringBuilder dataBuilder;
    private boolean lastEscapeCharacter;
    private Xkey from;
    private Xkey via;
    private Xkey to;
    
    private void resetDataBuilder() {
        dataBuilder = new StringBuilder();
        lastEscapeCharacter = false;
    }

    private void directFinish(int i) throws ProcessorException {
        from.add(to);
        dataState = DataState.PENDING;
        resetDataBuilder();
        state = switch (i) {
            case ']' -> State.DIRECT;
            case '[' -> State.VIA_NODE;
            case '#' -> State.AT;
            default -> throw new ProcessorException();
        };
    }

    private void viaFinish(int i) throws ProcessorException {
        dataState = DataState.PENDING;
        resetDataBuilder();
        state = switch (i) {
            case ']' -> State.TO_NODE;
            default -> throw new ProcessorException();
        };
    }

    private void toFinish(int i) throws ProcessorException {
        from.set(via, to);
        dataState = DataState.PENDING;
        resetDataBuilder();
        state = switch (i) {
            case '[' -> State.VIA_NODE;
            case ']' -> State.DIRECT;
            case '#' -> State.AT;
            default -> throw new ProcessorException();
        };
    }

    private void pendingDataState(int i) throws ProcessorException {
        if(Character.isJavaIdentifierStart(i)) {
            dataState = DataState.HUMBLE_STRING;
            dataBuilder.appendCodePoint(i);
        } else if(i == '#') {
            dataState = DataState.REFERENCE;
        } else if(i == '"') {
            dataState = DataState.STRING;
        } else if(Character.isDigit(i)) {
            dataState = DataState.NUMBER;
            dataBuilder.appendCodePoint(i);
        } else if(i == '.' || i == ',') {
            dataState = DataState.REAL_NUMBER;
            dataBuilder.append("0.");
        } else if(i == '-') {
            dataState = DataState.MINUS;
        } else if(i == '+') {
            dataState = DataState.PLUS;
        } else if(isEscapeCharacter(i)) {
            dataState = DataState.HUMBLE_STRING;
            lastEscapeCharacter = true;
        } else if(!Character.isWhitespace(i)){
            throw new ProcessorException("Invalid input");
        }
    }

    public static boolean isControlCharacter(int codePoint) {
        return codePoint == ']' || codePoint == '#' || codePoint == '[';
    }

    public static boolean isEscapeCharacter(int codePoint) {
        return codePoint == '`';
    }

    @Override
    public Subject ready() {
        keys = Suite.set();
        Reference zero = new Reference("0", true);
        from = new Xkey(null, zero, false);
        keys.set(zero, from);
        state = State.DIRECT;
        dataState = DataState.PENDING;
        resetDataBuilder();
        return Suite.set();
    }

    public void advance(int i) throws ProcessorException {
        switch (state) {
            case AT:
                if(i == '[') {
                    state = State.FROM_NODE;
                    dataState = DataState.PENDING;
                    resetDataBuilder();
                } else if(i == ']') {
                    to = terminatorXkey;
                    directFinish(i);
                } else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input '" + new StringBuilder().appendCodePoint(i) + "'");
                }
                break;

            case FROM_NODE:
                if(lastEscapeCharacter) {
                    dataBuilder.appendCodePoint(i);
                    lastEscapeCharacter = false;
                } else if(isControlCharacter(i)) {
                    String string = dataBuilder.toString().trim();
                    if(string.isEmpty()) throw new ProcessorException("Empty reference");
                    Reference reference = new Reference(string);
                    from = keys.getSaved(reference, new Xkey(null, reference, false)).asExpected();
                    ((Reference)from.getLabel()).setDeclared(true);
                    dataState = DataState.PENDING;
                    resetDataBuilder();
                    state = switch (i) {
                        case ']' -> State.DIRECT;
                        case '[' -> State.VIA_NODE;
                        case '#' -> State.AT;
                        default -> throw new ProcessorException();
                    };
                } else if(isEscapeCharacter(i)) {
                    lastEscapeCharacter = true;
                } else {
                    dataBuilder.appendCodePoint(i);
                }
                break;

            case DIRECT:
                switch (dataState) {
                    case PENDING:
                        if(Character.isJavaIdentifierStart(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '#') {
                            dataState = DataState.REFERENCE;
                        } else if(i == '"') {
                            dataState = DataState.STRING;
                        } else if(Character.isDigit(i)) {
                            dataState = DataState.NUMBER;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(i == '-') {
                            dataState = DataState.MINUS;
                        } else if(i == '+') {
                            dataState = DataState.PLUS;
                        } else if(isEscapeCharacter(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            lastEscapeCharacter = true;
                        } else if(i == '[') {
                            state = State.VIA_NODE;
                        } else if(i == ']') {
                            from.add(nullXkey);
                        } else if(!Character.isWhitespace(i)) {
                                throw new ProcessorException("Invalid input");
                        }
                        break;

                    case HUMBLE_STRING:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(isControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null, true)).asExpected();
                            directFinish(i);
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(isControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if (string.isEmpty()) {
                                if (i == '[') {
                                    state = State.FROM_NODE;
                                } else if(i == ']') {
                                    to = nullXkey;
                                    directFinish(i);
                                    to = terminatorXkey;
                                    directFinish(i);
                                } else {
                                    throw new ProcessorException();
                                }
                            } else {
                                Reference reference = new Reference(string);
                                to = keys.getSaved(reference, new Xkey(null, reference, false)).asExpected();
                                directFinish(i);
                            }
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(i == '"') {
                            String string = dataBuilder.toString();
                            to = keys.getSaved(string, new Xkey(string, null, true)).asExpected();
                            from.add(to);
                            dataState = DataState.PENDING;
                            resetDataBuilder();
                            state = State.AFTER_DIRECT;
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null, true)).asExpected();
                            directFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            to = keys.getSaved(d, new Xkey(d, null, true)).asExpected();
                            directFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case MINUS:
                        if(Character.isDigit(i)) {
                            dataBuilder.append('-').appendCodePoint(i);
                            dataState = DataState.NUMBER;
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("-0.");
                        } else if(isControlCharacter(i)) {
                            to = keys.getSaved(false, new Xkey(false, null, true)).asExpected();
                            directFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case PLUS:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                            dataState = DataState.NUMBER;
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(isControlCharacter(i)) {
                            to = keys.getSaved(true, new Xkey(true, null, true)).asExpected();
                            directFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                }
                break;

            case AFTER_DIRECT:
                state = switch (i) {
                    case ']' -> State.DIRECT;
                    case '[' -> State.VIA_NODE;
                    case '#' -> State.AT;
                    default -> {
                        if(Character.isWhitespace(i)){
                            yield State.AFTER_DIRECT;
                        }
                        throw new ProcessorException();
                    }
                };
                break;

            case VIA_NODE:
                switch (dataState) {
                    case PENDING:
                        if(Character.isJavaIdentifierStart(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '@') {
                            dataState = DataState.REFERENCE;
                        } else if(i == '"') {
                            dataState = DataState.STRING;
                        } else if(Character.isDigit(i)) {
                            dataState = DataState.NUMBER;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(i == '-') {
                            dataState = DataState.MINUS;
                        } else if(i == '+') {
                            dataState = DataState.PLUS;
                        } else if(isEscapeCharacter(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            lastEscapeCharacter = true;
                        } else if(i == ']') {
                            via = nullXkey;
                            viaFinish(i);
                        } else if(!Character.isWhitespace(i)){
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case HUMBLE_STRING:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(isControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            via = keys.getSaved(string, new Xkey(string, null, true)).asExpected();
                            viaFinish(i);
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(isControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                throw new ProcessorException();
                            } else {
                                Reference reference = new Reference(string);
                                via = keys.getSaved(reference, new Xkey(null, reference, false)).asExpected();
                                viaFinish(i);
                            }
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(i == '"') {
                            String string = dataBuilder.toString();
                            via = keys.getSaved(string, new Xkey(string, null, true)).asExpected();
                            dataState = DataState.PENDING;
                            resetDataBuilder();
                            state = State.AFTER_VIA_NODE;
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            via = keys.getSaved(integer, new Xkey(integer, null, true)).asExpected();
                            viaFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            via = keys.getSaved(d, new Xkey(d, null, true)).asExpected();
                            viaFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;


                    case MINUS:
                        if(Character.isDigit(i)) {
                            dataBuilder.append('-').appendCodePoint(i);
                            dataState = DataState.NUMBER;
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("-0.");
                        } else if(isControlCharacter(i)) {
                            via = keys.getSaved(false, new Xkey(false, null, true)).asExpected();
                            viaFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case PLUS:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                            dataState = DataState.NUMBER;
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(isControlCharacter(i)) {
                            via = keys.getSaved(true, new Xkey(true, null, true)).asExpected();
                            viaFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                }
                break;

            case AFTER_VIA_NODE:
                state = switch (i) {
                    case ']' -> State.TO_NODE;
                    case '[' -> throw new ProcessorException();
                    case '#' -> throw new ProcessorException();
                    default -> {
                        if(Character.isWhitespace(i)){
                            yield State.AFTER_VIA_NODE;
                        }
                        throw new ProcessorException();
                    }
                };
                break;

            case TO_NODE:
                switch (dataState) {
                    case PENDING:
                        pendingDataState(i);
                        break;

                    case HUMBLE_STRING:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(isControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null, true)).asExpected();
                            toFinish(i);
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(isControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                throw new ProcessorException();
                            } else {
                                Reference reference = new Reference(string);
                                to = keys.getSaved(reference, new Xkey(null, reference, false)).asExpected();
                                toFinish(i);
                            }
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                            lastEscapeCharacter = false;
                        } else if(i == '"') {
                            String string = dataBuilder.toString();
                            to = keys.getSaved(string, new Xkey(string, null, true)).asExpected();
                            from.set(via, to);
                            dataState = DataState.PENDING;
                            resetDataBuilder();
                            state = State.AFTER_TO_NODE;
                        } else if(isEscapeCharacter(i)) {
                            lastEscapeCharacter = true;
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null, true)).asExpected();
                            toFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            to = keys.getSaved(d, new Xkey(d, null, true)).asExpected();
                            toFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case MINUS:
                        if(Character.isDigit(i)) {
                            dataBuilder.append('-').appendCodePoint(i);
                            dataState = DataState.NUMBER;
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("-0.");
                        } else if(isControlCharacter(i)) {
                            to = keys.getSaved(false, new Xkey(false, null, true)).asExpected();
                            toFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case PLUS:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                            dataState = DataState.NUMBER;
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(isControlCharacter(i)) {
                            to = keys.getSaved(true, new Xkey(true, null, true)).asExpected();
                            toFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                }
                break;

            case AFTER_TO_NODE:
                state = switch (i) {
                    case ']' -> State.DIRECT;
                    case '[' -> State.VIA_NODE;
                    case '#' -> State.AT;
                    default -> {
                        if(Character.isWhitespace(i)){
                            yield State.AFTER_TO_NODE;
                        }
                        throw new ProcessorException();
                    }
                };
                break;
        }
    }

    @Override
    public Subject finish() throws ProcessorException {
        String str;
        switch (state) {
            case FROM_NODE -> {
                str = dataBuilder.toString().trim();
                if (str.isEmpty()) {
                    throw new ProcessorException();
                } else {
                    Reference reference = new Reference(str);
                    from = keys.getSaved(reference, new Xkey(null, reference, false)).asExpected();
                    ((Reference) from.getLabel()).setDeclared(true);
                }
            }
            case DIRECT, TO_NODE -> {
                switch (dataState) {
                    case HUMBLE_STRING -> {
                        str = dataBuilder.toString().trim();
                        to = keys.getSaved(str, new Xkey(str, null, true)).asExpected();
                    }

                    case REFERENCE -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Reference reference = new Reference(str);
                            to = keys.getSaved(reference, new Xkey(null, reference, false)).asExpected();
                        }
                    }

                    case STRING -> {
                        str = dataBuilder.toString();
                        to = keys.getSaved(str, new Xkey(str, null, true)).asExpected();
                    }

                    case NUMBER -> {
                        str = dataBuilder.toString();
                        int integer = Integer.parseInt(str);
                        to = keys.getSaved(integer, new Xkey(integer, null, true)).asExpected();
                    }

                    case REAL_NUMBER -> {
                        str = dataBuilder.toString();
                        double d = Double.parseDouble(str);
                        to = keys.getSaved(d, new Xkey(d, null, true)).asExpected();
                    }

                    case MINUS -> to = keys.getSaved(false, new Xkey(false, null, true)).asExpected();

                    case PLUS -> to = keys.getSaved(true, new Xkey(true, null, true)).asExpected();

                }
                if (dataState != DataState.PENDING) {
                    if (state == State.DIRECT) from.add(to);
                    else from.set(via, to);
                }
            }
        }
        return keys;
    }
}
