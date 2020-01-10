package app.core.agent;

import app.core.suite.Subject;

@FunctionalInterface
public interface Service {
    Subject fulfil(Subject subject);
}
