package app.modules.model;

import app.core.suite.Subject;

public interface IntSerialProcessor {
    Subject ready();
    Subject advance(int i);
    Subject finish();
}
