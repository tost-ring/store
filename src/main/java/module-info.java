open module store.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires javafx.graphics;
    requires java.sql;
//    requires org.apache.commons.text;
    exports app;
    exports app.core.suite;
    exports app.core.suite.action;
    exports app.core.agent;
    exports app.core.flow;
    exports app.core;
}