package app.modules.model;

import app.core.jorg.Xkey;
import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgProcessor implements IntProcessor {

    enum State {
        BEFORE_FROM_NODE, FROM_NODE, DIRECT_PRE, AFTER_DIRECT_PRE, VIA_NODE,
        AFTER_VIA_NODE, TO_NODE, AFTER_TO_NODE, TABLE_PORT_SIZE, TABLE_PORT, DIRECT_POST, AFTER_DIRECT_POST
    }

    enum DataState {
        PENDING, HUMBLE_STRING, STRING, REFERENCE, PORT, NUMBER, REAL_NUMBER
    }

    private Subject keys;
    private State state;
    private DataState dataState;
    private StringBuilder dataBuilder;
    private boolean lastEscapeCharacter;
    private int tableSize;
    private Xkey from;
    private Xkey via;
    private Xkey to;
    
    private void resetDataBuilder() {
        dataBuilder = new StringBuilder();
        lastEscapeCharacter = false;
    }

    private void directPreFinish(int i) throws ProcessorException {
        from.addPre(to);
        dataState = DataState.PENDING;
        resetDataBuilder();
        state = switch (i) {
            case ']' -> State.DIRECT_PRE;
            case '[' -> State.VIA_NODE;
            case '@' -> State.BEFORE_FROM_NODE;
            default -> throw new ProcessorException();
        };
    }

    private void directPostFinish(int i) throws ProcessorException {
        from.addPost(to);
        dataState = DataState.PENDING;
        resetDataBuilder();
        state = switch (i) {
            case ']' -> State.DIRECT_POST;
            case '[' -> State.VIA_NODE;
            case '@' -> State.BEFORE_FROM_NODE;
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
        from.setPost(via, to);
        dataState = DataState.PENDING;
        resetDataBuilder();
        state = switch (i) {
            case '[' -> State.VIA_NODE;
            case ']' -> State.DIRECT_POST;
            case '@' -> State.BEFORE_FROM_NODE;
            default -> throw new ProcessorException();
        };
    }

    private void pendingDataState(int i) throws ProcessorException {
        if(Character.isJavaIdentifierStart(i)) {
            dataState = DataState.HUMBLE_STRING;
            dataBuilder.appendCodePoint(i);
        } else if(i == '@') {
            dataState = DataState.REFERENCE;
        } else if(i == '"') {
            dataState = DataState.STRING;
        } else if(i == '#') {
            dataState = DataState.PORT;
        } else if(Character.isDigit(i) || i == '-') {
            dataState = DataState.NUMBER;
            dataBuilder.appendCodePoint(i);
        } else if(i == '.' || i == ',') {
            dataState = DataState.REAL_NUMBER;
            dataBuilder.append("0.");
        } else if(!Character.isWhitespace(i)){
            throw new ProcessorException("Invalid input");
        }
    }

    public static boolean isJorgControlCharacter(int codePoint) {
        return codePoint == '[' || codePoint == ']' || codePoint == '@' || codePoint == '#';
    }

    @Override
    public Subject ready() {
        keys = Suite.set();
        from = new Xkey(null, new Reference(""));
        keys.set(0, from);
        state = State.DIRECT_PRE;
        dataState = DataState.PENDING;
        resetDataBuilder();
        return Suite.set();
    }

    public Subject advance(int i) throws ProcessorException {
        switch (state) {
            case BEFORE_FROM_NODE:
                if(i == '[') {
                    state = State.FROM_NODE;
                    dataState = DataState.PENDING;
                    resetDataBuilder();
                } else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input");
                }
                break;
            case FROM_NODE:
                if(isJorgControlCharacter(i)) {
                    String string = dataBuilder.toString().trim();
                    if(string.isEmpty()) throw new ProcessorException("Empty reference");
                    Reference reference = new Reference(dataBuilder.toString().trim());
                    from = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                    dataState = DataState.PENDING;
                    resetDataBuilder();
                    state = switch (i) {
                        case ']' -> State.DIRECT_PRE;
                        case '[' -> State.VIA_NODE;
                        case '@' -> State.BEFORE_FROM_NODE;
                        default -> throw new ProcessorException();
                    };
                } else {
                    dataBuilder.appendCodePoint(i);
                }
                break;
            case DIRECT_PRE:
                switch (dataState) {
                    case PENDING:
                        if(Character.isJavaIdentifierStart(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '@') {
                            dataState = DataState.REFERENCE;
                        } else if(i == '"') {
                            dataState = DataState.STRING;
                        } else if(i == '#') {
                            dataState = DataState.PORT;
                        } else if(Character.isDigit(i) || i == '-') {
                            dataState = DataState.NUMBER;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(i == '[') {
                            state = State.VIA_NODE;
                        } else if(i == ']') {
                            from.addPre(null);
                        } else if(!Character.isWhitespace(i)) {
                                throw new ProcessorException("Invalid input");
                        }
                        break;

                    case HUMBLE_STRING:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            directPreFinish(i);
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                if(i == '[') {
                                    state = State.FROM_NODE;
                                } else {
                                    throw new ProcessorException();
                                }
                            } else {
                                Reference reference = new Reference(string);
                                to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                                directPreFinish(i);
                            }
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(i == '"') {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString();
                                    to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                                    from.addPre(to);
                                    dataState = DataState.PENDING;
                                    resetDataBuilder();
                                    state = State.AFTER_DIRECT_PRE;
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case PORT:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(isJorgControlCharacter(i)) {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString().trim();
                                    if(string.isEmpty()) {
                                        if(i == '[') {
                                            state = State.TABLE_PORT_SIZE;
                                        } else {
                                            throw new ProcessorException();
                                        }
                                    } else {
                                        Port port = new Port(string);
                                        to = keys.getSaved(port, new Xkey(null, port)).asExpected();
                                        directPreFinish(i);
                                    }
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isJorgControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            directPreFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isJorgControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            directPreFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                }
                break;

            case AFTER_DIRECT_PRE:
                state = switch (i) {
                    case ']' -> State.DIRECT_PRE;
                    case '[' -> State.VIA_NODE;
                    case '@' -> State.BEFORE_FROM_NODE;
                    default -> {
                        if(Character.isWhitespace(i)){
                            yield State.AFTER_DIRECT_PRE;
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
                        } else if(i == '#') {
                            dataState = DataState.PORT;
                        } else if(Character.isDigit(i) || i == '-') {
                            dataState = DataState.NUMBER;
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append("0.");
                        } else if(i == ']') {
                            state = State.DIRECT_POST;
                        } else if(!Character.isWhitespace(i)){
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case HUMBLE_STRING:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            via = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            viaFinish(i);
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                throw new ProcessorException();
                            } else {
                                Reference reference = new Reference(string);
                                via = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                                viaFinish(i);
                            }
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(i == '"') {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString();
                                    via = keys.getSaved(string, new Xkey(string, null)).asExpected();
                                    dataState = DataState.PENDING;
                                    resetDataBuilder();
                                    state = State.AFTER_VIA_NODE;
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case PORT:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(isJorgControlCharacter(i)) {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString().trim();
                                    if(string.isEmpty()) {
                                        throw new ProcessorException();
                                    } else {
                                        Port port = new Port(string);
                                        via = keys.getSaved(port, new Xkey(null, port)).asExpected();
                                        viaFinish(i);
                                    }
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isJorgControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            via = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            viaFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isJorgControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            via = keys.getSaved(d, new Xkey(d, null)).asExpected();
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
                    case '@' -> throw new ProcessorException();
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
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            toFinish(i);
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                throw new ProcessorException();
                            } else {
                                Reference reference = new Reference(string);
                                to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                                toFinish(i);
                            }
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(i == '"') {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString();
                                    to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                                    from.setPost(via, to);
                                    dataState = DataState.PENDING;
                                    resetDataBuilder();
                                    state = State.AFTER_TO_NODE;
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case PORT:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(isJorgControlCharacter(i)) {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString().trim();
                                    if(string.isEmpty()) {
                                        throw new ProcessorException();
                                    } else {
                                        Port port = new Port(string);
                                        to = keys.getSaved(port, new Xkey(null, port)).asExpected();
                                        toFinish(i);
                                    }
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isJorgControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            toFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isJorgControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            toFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                }
                break;

            case AFTER_TO_NODE:
                state = switch (i) {
                    case ']' -> State.DIRECT_POST;
                    case '[' -> State.VIA_NODE;
                    case '@' -> State.BEFORE_FROM_NODE;
                    default -> {
                        if(Character.isWhitespace(i)){
                            yield State.AFTER_TO_NODE;
                        }
                        throw new ProcessorException();
                    }
                };
                break;

            case TABLE_PORT_SIZE:
                if(Character.isDigit(i)) {
                    dataBuilder.appendCodePoint(i);
                } else if(isJorgControlCharacter(i)) {
                    tableSize = Integer.parseInt(dataBuilder.toString());
                    resetDataBuilder();
                    dataState = DataState.PORT;
                    if(i == ']') {
                        state = State.TABLE_PORT;
                    } else {
                        throw new ProcessorException();
                    }
                } else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input");
                }
                break;

            case TABLE_PORT:
                if(i == '`') {
                    if(lastEscapeCharacter) {
                        dataBuilder.appendCodePoint(i);
                        lastEscapeCharacter = false;
                    } else {
                        lastEscapeCharacter = true;
                    }
                } else {
                    if(isJorgControlCharacter(i)) {
                        if(lastEscapeCharacter) {
                            dataBuilder.appendCodePoint(i);
                        } else {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                throw new ProcessorException();
                            } else {
                                TablePort tablePort = new TablePort(tableSize, string);
                                to = keys.getSaved(tablePort, new Xkey(null, tablePort)).asExpected();
                                from.addPre(to);
                                dataState = DataState.PENDING;
                                resetDataBuilder();
                                state = switch (i) {
                                    case ']' -> State.DIRECT_POST;
                                    case '[' -> State.VIA_NODE;
                                    case '@' -> State.BEFORE_FROM_NODE;
                                    default -> throw new ProcessorException();
                                };
                            }
                        }
                    } else {
                        dataBuilder.appendCodePoint(i);
                    }
                    lastEscapeCharacter = false;
                }
                break;

            case DIRECT_POST:
                switch (dataState) {
                    case PENDING:
                        pendingDataState(i);
                        break;

                    case HUMBLE_STRING:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            directPostFinish(i);
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case REFERENCE:
                        if(isJorgControlCharacter(i)) {
                            String string = dataBuilder.toString().trim();
                            if(string.isEmpty()) {
                                throw new ProcessorException();
                            } else {
                                Reference reference = new Reference(string);
                                to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                                directPostFinish(i);
                            }
                        } else {
                            dataBuilder.appendCodePoint(i);
                        }
                        break;

                    case STRING:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(i == '"') {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString();
                                    to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                                    from.setPost(via, to);
                                    dataState = DataState.PENDING;
                                    resetDataBuilder();
                                    state = State.AFTER_TO_NODE;
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case PORT:
                        if(i == '`') {
                            if(lastEscapeCharacter) {
                                dataBuilder.appendCodePoint(i);
                                lastEscapeCharacter = false;
                            } else {
                                lastEscapeCharacter = true;
                            }
                        } else {
                            if(isJorgControlCharacter(i)) {
                                if(lastEscapeCharacter) {
                                    dataBuilder.appendCodePoint(i);
                                } else {
                                    String string = dataBuilder.toString().trim();
                                    if(string.isEmpty()) {
                                        throw new ProcessorException();
                                    } else {
                                        Port port = new Port(string);
                                        to = keys.getSaved(port, new Xkey(null, port)).asExpected();
                                        directPostFinish(i);
                                    }
                                }
                            } else {
                                dataBuilder.appendCodePoint(i);
                            }
                            lastEscapeCharacter = false;
                        }
                        break;

                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuilder.append('.');
                        } else if(isJorgControlCharacter(i)) {
                            int integer = Integer.parseInt(dataBuilder.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            directPostFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuilder.appendCodePoint(i);
                        } else if(isJorgControlCharacter(i)) {
                            double d = Double.parseDouble(dataBuilder.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            directPostFinish(i);
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;

                }
                break;

            case AFTER_DIRECT_POST:
                state = switch (i) {
                    case ']' -> State.DIRECT_POST;
                    case '[' -> State.VIA_NODE;
                    case '@' -> State.BEFORE_FROM_NODE;
                    default -> {
                        if(Character.isWhitespace(i)){
                            yield State.AFTER_DIRECT_POST;
                        }
                        throw new ProcessorException();
                    }
                };
                break;

        }
        return Suite.set();
    }

    @Override
    public Subject finish() throws ProcessorException {
        String str;
        switch (state) {
            case FROM_NODE:
                str = dataBuilder.toString().trim();
                if(str.isEmpty()) {
                    throw new ProcessorException();
                } else {
                    Reference reference = new Reference(str);
                    from = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                }
                break;

            case DIRECT_PRE:
                switch (dataState) {
                    case HUMBLE_STRING -> {
                        str = dataBuilder.toString().trim();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.addPre(to);
                    }
                    case REFERENCE -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Reference reference = new Reference(str);
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.addPre(to);
                        }
                    }
                    case STRING -> {
                        str = dataBuilder.toString();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.addPre(to);
                    }
                    case PORT -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Port port = new Port(str);
                            to = keys.getSaved(port, new Xkey(null, port)).asExpected();
                            from.addPre(to);
                        }
                    }
                    case NUMBER -> {
                        str = dataBuilder.toString();
                        int integer = Integer.parseInt(str);
                        to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                        from.addPre(to);
                    }
                    case REAL_NUMBER -> {
                        str = dataBuilder.toString();
                        double d = Double.parseDouble(str);
                        to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                        from.addPre(to);
                    }
                }
                break;

            case DIRECT_POST:
                switch (dataState) {
                    case HUMBLE_STRING -> {
                        str = dataBuilder.toString().trim();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.addPost(to);
                    }
                    case REFERENCE -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Reference reference = new Reference(str);
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.addPost(to);
                        }
                    }
                    case STRING -> {
                        str = dataBuilder.toString();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.addPost(to);
                    }
                    case PORT -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Port port = new Port(str);
                            to = keys.getSaved(port, new Xkey(null, port)).asExpected();
                            from.addPost(to);
                        }
                    }
                    case NUMBER -> {
                        str = dataBuilder.toString();
                        int integer = Integer.parseInt(str);
                        to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                        from.addPost(to);
                    }
                    case REAL_NUMBER -> {
                        str = dataBuilder.toString();
                        double d = Double.parseDouble(str);
                        to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                        from.addPost(to);
                    }
                }
                break;

            case TO_NODE:
                switch (dataState) {
                    case HUMBLE_STRING -> {
                        str = dataBuilder.toString().trim();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.setPost(via, to);
                    }
                    case REFERENCE -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Reference reference = new Reference(str);
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.setPost(via, to);
                        }
                    }
                    case STRING -> {
                        str = dataBuilder.toString();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.setPost(via, to);
                    }
                    case PORT -> {
                        str = dataBuilder.toString().trim();
                        if (str.isEmpty()) {
                            throw new ProcessorException();
                        } else {
                            Port port = new Port(str);
                            to = keys.getSaved(port, new Xkey(null, port)).asExpected();
                            from.setPost(via, to);
                        }
                    }
                    case NUMBER -> {
                        str = dataBuilder.toString();
                        int integer = Integer.parseInt(str);
                        to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                        from.setPost(via, to);
                    }
                    case REAL_NUMBER -> {
                        str = dataBuilder.toString();
                        double d = Double.parseDouble(str);
                        to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                        from.setPost(via, to);
                    }
                }
                break;

        }
        return keys;
    }
}
