package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

public class HumbleStringSerialProcessor implements IntSerialProcessor {

    private StringBuilder builder;

    @Override
    public Subject ready() {
        builder = new StringBuilder();
        return Suite.set();
    }

    @Override
    public Subject advance(int i) {
        if(builder.length() == 0) {
            if(Character.isLetter(i)) {
                builder.appendCodePoint(i);
                return Suite.set();
            } else {
                return Suite.error("First symbol is not letter");
            }
        } else {
            if(Character.isLetterOrDigit(i) || i == '_') {
                builder.appendCodePoint(i);
                return Suite.set();
            } else {
                return Suite.error("Invalid symbol given");
            }
        }
    }

    @Override
    public Subject finish() {
        if(builder.length() > 0) {
            return Suite.set(builder.toString());
        } else {
            return Suite.set();
        }
    }
}
