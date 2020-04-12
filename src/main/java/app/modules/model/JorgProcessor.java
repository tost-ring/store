package app.modules.model;

import app.core.jorg.Xkey;
import app.core.suite.Subject;
import app.core.suite.Suite;

public class JorgProcessor implements IntProcessor {

    enum State {
        BEFORE_FROM_NODE, FROM_NODE, DIRECT_TO_NODE, AFTER_DIRECT_TO_NODE, VIA_NODE,
        AFTER_VIA_NODE, TO_NODE, AFTER_TO_NODE
    }

    enum DataState {
        PENDING, HUMBLE_STRING, STRING, REFERENCE, SOCKET, NUMBER, REAL_NUMBER
    }

    private Subject keys;
    private State state;
    private DataState dataState;
    private StringBuilder dataBuffer;
    private Xkey from;
    private Xkey via;
    private Xkey to;

    @Override
    public Subject ready() {
        keys = Suite.set();
        from = new Xkey(null, 0);
        keys.set(0, from);
        state = State.AFTER_DIRECT_TO_NODE;
        return Suite.set();
    }

    public Subject advance(int i) throws ProcessorException {
        switch (state) {
            case BEFORE_FROM_NODE:
                if(i == '[') {
                    state = State.FROM_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input");
                }
                break;
            case FROM_NODE:
                if(i == ']') {
                    Reference reference = new Reference(dataBuffer.toString().trim());
                    from = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                    state = State.DIRECT_TO_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else if(i == '[') {
                    Reference reference = new Reference(dataBuffer.toString().trim());
                    from = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                    state = State.VIA_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else {
                    dataBuffer.appendCodePoint(i);
                }
                break;
            case DIRECT_TO_NODE:
                switch (dataState) {
                    case PENDING:
                        if(Character.isJavaIdentifierStart(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '@') {
                            dataState = DataState.REFERENCE;
                        } else if(i == '"') {
                            dataState = DataState.STRING;
                        } else if(i == '#') {
                            dataState = DataState.SOCKET;
                        } else if(Character.isDigit(i) || i == '-') {
                            dataState = DataState.NUMBER;
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuffer.append("0.");
                        } else if(i == '[') {
                            state = State.VIA_NODE;
                        } else if(i == ']') {
                            from.getDirect().add(null);
                        } else if(!Character.isWhitespace(i)) {
                                throw new ProcessorException("Invalid input");
                        }
                        break;
                    case HUMBLE_STRING:
                        if(i == ']') {
                            String string = dataBuffer.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.DIRECT_TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            String string = dataBuffer.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.VIA_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else {
                            dataBuffer.appendCodePoint(i);
                        }
                        break;
                    case REFERENCE:
                        if(i == ']') {
                            Reference reference = new Reference(dataBuffer.toString().trim());
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.getDirect().add(to);
                            state = State.DIRECT_TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            String string = dataBuffer.toString().trim();
                            if(string.isEmpty()) {
                                state = State.FROM_NODE;
                            } else {
                                Reference reference = new Reference(dataBuffer.toString().trim());
                                to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                                from.getDirect().add(to);
                                state = State.VIA_NODE;
                                dataState = DataState.PENDING;
                                dataBuffer = new StringBuilder();
                            }
                        } else {
                            dataBuffer.appendCodePoint(i);
                        }
                        break;
                    case STRING:
                        if(i != '"') {
                            dataBuffer.appendCodePoint(i);
                        } else {
                            String string = dataBuffer.toString();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.AFTER_DIRECT_TO_NODE;
                        }
                        break;
                    case SOCKET:
                        if(i != '#') {
                            dataBuffer.appendCodePoint(i);
                        } else {
                            Socket socket = new Socket(dataBuffer.toString());
                            to = keys.getSaved(socket, new Xkey(null, socket)).asExpected();
                            from.getDirect().add(to);
                            state = State.AFTER_DIRECT_TO_NODE;
                        }
                        break;
                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuffer.append('.');
                        } else if(i == ']') {
                            int integer = Integer.parseInt(dataBuffer.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.DIRECT_TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            int integer = Integer.parseInt(dataBuffer.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.VIA_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuffer.appendCodePoint(i);
                        } else if(i == ']') {
                            double d = Double.parseDouble(dataBuffer.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.DIRECT_TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            double d = Double.parseDouble(dataBuffer.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            from.getDirect().add(to);
                            state = State.VIA_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                }
                break;
            case AFTER_DIRECT_TO_NODE:
                if(i == ']') {
                    state = State.DIRECT_TO_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else if(i == '[') {
                    state = State.VIA_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else if(i == '@') {
                    state = State.BEFORE_FROM_NODE;
                }else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input");
                }
                break;
            case VIA_NODE:
                switch (dataState) {
                    case PENDING:
                        if(Character.isJavaIdentifierStart(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '@') {
                            dataState = DataState.REFERENCE;
                        } else if(i == '"') {
                            dataState = DataState.STRING;
                        } else if(i == '#') {
                            dataState = DataState.SOCKET;
                        } else if(Character.isDigit(i) || i == '-') {
                            dataState = DataState.NUMBER;
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuffer.append("0.");
                        } else {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                    case HUMBLE_STRING:
                        if(i == ']') {
                            String string = dataBuffer.toString().trim();
                            via = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else {
                            dataBuffer.appendCodePoint(i);
                        }
                        break;
                    case REFERENCE:
                        if(i == ']') {
                            Reference reference = new Reference(dataBuffer.toString().trim());
                            via = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else {
                            dataBuffer.appendCodePoint(i);
                        }
                        break;
                    case STRING:
                        if(i != '"') {
                            dataBuffer.appendCodePoint(i);
                        } else {
                            String string = dataBuffer.toString();
                            via = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            state = State.AFTER_VIA_NODE;
                        }
                        break;
                    case SOCKET:
                        if(i != '#') {
                            dataBuffer.appendCodePoint(i);
                        } else {
                            Socket socket = new Socket(dataBuffer.toString());
                            via = keys.getSaved(socket, new Xkey(null, socket)).asExpected();
                            state = State.AFTER_VIA_NODE;
                        }
                        break;
                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuffer.append('.');
                        } else if(i == ']') {
                            int integer = Integer.parseInt(dataBuffer.toString());
                            via = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuffer.appendCodePoint(i);
                        } else if(i == ']') {
                            double d = Double.parseDouble(dataBuffer.toString());
                            via = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                }
                break;
            case AFTER_VIA_NODE:
                if(i == ']') {
                    state = State.TO_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input");
                }
                break;
            case TO_NODE:
                switch (dataState) {
                    case PENDING:
                        if(Character.isJavaIdentifierStart(i)) {
                            dataState = DataState.HUMBLE_STRING;
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '@') {
                            dataState = DataState.REFERENCE;
                        } else if(i == '"') {
                            dataState = DataState.STRING;
                        } else if(i == '#') {
                            dataState = DataState.SOCKET;
                        } else if(Character.isDigit(i) || i == '-') {
                            dataState = DataState.NUMBER;
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuffer.append("0.");
                        } else {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                    case HUMBLE_STRING:
                        if(i == ']') {
                            String string = dataBuffer.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            String string = dataBuffer.toString().trim();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.VIA_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else {
                            dataBuffer.appendCodePoint(i);
                        }
                        break;
                    case REFERENCE:
                        if(i == ']') {
                            Reference reference = new Reference(dataBuffer.toString().trim());
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            String string = dataBuffer.toString().trim();
                            if(string.isEmpty()) {
                                state = State.FROM_NODE;
                            } else {
                                Reference reference = new Reference(string);
                                to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                                from.getGraph().add(via, to);
                                state = State.VIA_NODE;
                                dataState = DataState.PENDING;
                                dataBuffer = new StringBuilder();
                            }
                        } else {
                            dataBuffer.appendCodePoint(i);
                        }
                        break;
                    case STRING:
                        if(i != '"') {
                            dataBuffer.appendCodePoint(i);
                        } else {
                            String string = dataBuffer.toString();
                            to = keys.getSaved(string, new Xkey(string, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.AFTER_TO_NODE;
                        }
                        break;
                    case SOCKET:
                        if(i != '#') {
                            dataBuffer.appendCodePoint(i);
                        } else {
                            Socket socket = new Socket(dataBuffer.toString());
                            to = keys.getSaved(socket, new Xkey(null, socket)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.AFTER_TO_NODE;
                        }
                        break;
                    case NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuffer.appendCodePoint(i);
                        } else if(i == '.' || i == ',') {
                            dataState = DataState.REAL_NUMBER;
                            dataBuffer.append('.');
                        } else if(i == ']') {
                            int integer = Integer.parseInt(dataBuffer.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            int integer = Integer.parseInt(dataBuffer.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.VIA_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '@') {
                            int integer = Integer.parseInt(dataBuffer.toString());
                            to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.BEFORE_FROM_NODE;
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                    case REAL_NUMBER:
                        if(Character.isDigit(i)) {
                            dataBuffer.appendCodePoint(i);
                        } else if(i == ']') {
                            double d = Double.parseDouble(dataBuffer.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.TO_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(i == '[') {
                            double d = Double.parseDouble(dataBuffer.toString());
                            to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                            from.getGraph().add(via, to);
                            state = State.VIA_NODE;
                            dataState = DataState.PENDING;
                            dataBuffer = new StringBuilder();
                        } else if(!Character.isWhitespace(i)) {
                            throw new ProcessorException("Invalid input");
                        }
                        break;
                }
                break;
            case AFTER_TO_NODE:
                if(i == ']') {
                    state = State.TO_NODE;
                    dataState = DataState.PENDING;
                    dataBuffer = new StringBuilder();
                } else if(i == '@') {
                    state = State.BEFORE_FROM_NODE;
                } else if(!Character.isWhitespace(i)) {
                    throw new ProcessorException("Invalid input");
                }
                break;
        }
        return Suite.set();
    }

    @Override
    public Subject finish() throws ProcessorException {
        String str;
        switch (state) {
            case FROM_NODE:
                str = dataBuffer.toString().trim();
                if(!str.isEmpty()) {
                    Reference reference = new Reference(str);
                    from = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                }
                break;
            case DIRECT_TO_NODE:
                switch (dataState) {
                    case HUMBLE_STRING:
                        str = dataBuffer.toString().trim();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.getDirect().add(to);
                        break;
                    case REFERENCE:
                        str = dataBuffer.toString().trim();
                        if(!str.isEmpty()) {
                            Reference reference = new Reference(str);
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.getDirect().add(to);
                        }
                        break;
                    case STRING:
                        str = dataBuffer.toString();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.getDirect().add(to);
                        break;
                    case SOCKET:
                        Socket socket = new Socket(dataBuffer.toString());
                        to = keys.getSaved(socket, new Xkey(null, socket)).asExpected();
                        from.getDirect().add(to);
                        break;
                    case NUMBER:
                        str = dataBuffer.toString();
                        int integer = Integer.parseInt(str);
                        to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                        from.getDirect().add(to);
                        break;
                    case REAL_NUMBER:
                        str = dataBuffer.toString();
                        double d = Double.parseDouble(str);
                        to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                        from.getDirect().add(to);
                        break;
                }
                break;
            case TO_NODE:
                switch (dataState) {
                    case HUMBLE_STRING:
                        str = dataBuffer.toString().trim();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.getGraph().add(via, to);
                        break;
                    case REFERENCE:
                        str = dataBuffer.toString().trim();
                        if(!str.isEmpty()) {
                            Reference reference = new Reference(str);
                            to = keys.getSaved(reference, new Xkey(null, reference)).asExpected();
                            from.getGraph().add(via, to);
                        }
                        break;
                    case STRING:
                        str = dataBuffer.toString();
                        to = keys.getSaved(str, new Xkey(str, null)).asExpected();
                        from.getGraph().add(via, to);
                        break;
                    case SOCKET:
                        Socket socket = new Socket(dataBuffer.toString());
                        to = keys.getSaved(socket, new Xkey(null, socket)).asExpected();
                        from.getGraph().add(via, to);
                        break;
                    case NUMBER:
                        str = dataBuffer.toString();
                        int integer = Integer.parseInt(str);
                        to = keys.getSaved(integer, new Xkey(integer, null)).asExpected();
                        from.getGraph().add(via, to);
                        break;
                    case REAL_NUMBER:
                        str = dataBuffer.toString();
                        double d = Double.parseDouble(str);
                        to = keys.getSaved(d, new Xkey(d, null)).asExpected();
                        from.getGraph().add(via, to);
                        break;
                }
                break;
        }
        return keys;
    }
}
