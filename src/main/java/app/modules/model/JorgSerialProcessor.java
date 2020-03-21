package app.modules.model;

import app.core.jorg.Xkey;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.graph.ReferenceHashGraph;

import java.util.ArrayList;
import java.util.List;

public class JorgSerialProcessor implements IntProcessor{

    enum State {
        ORDER, DATA
    }

    static class JorgGraphBuilder {

        void appendData(Subject subject) {

        }

        void setDataMode() {

        }

        void setRowMode() {

        }

        void setTableMode() {

        }


    }

    private JorgGraphBuilder builder;
    private JorgDataProcessor dataProcessor = new JorgDataProcessor();
    private State state;
    private Subject current;
    private int codeCounter;

    @Override
    public Subject ready() {
        builder = new JorgGraphBuilder();
        dataProcessor.ready();
        codeCounter = 0;
        current = Suite.set();
        return Suite.set();
    }

    public Subject advance(int i) throws ProcessorException {
        ++codeCounter;
        if(state == State.DATA) {
            var advResult = dataProcessor.advance(i);
            if(advResult.is(ResultType.UNSUPPORTED)) {
                builder.appendData(dataProcessor.finish());
                state = State.ORDER;
                for(Subject s : advResult.getAs(ResultType.UNSUPPORTED, Subject.class)) {
                    advance(s.get());
                }
            } else if(advResult.is(ResultType.RESULT_READY)) {
                builder.appendData(dataProcessor.finish());
                state = State.ORDER;
            }
        } else {
            if(i == ']'){
                builder.setDataMode();
            } else if(i == '[') {
                builder.setRowMode();
            } else if(i == '$') {
                builder.setTableMode();
            } else if(Character.isWhitespace(i)) {

            } else throw new ProcessorException("Unexpected code point " + Character.toString(i) + " at " + codeCounter);
        }
        return Suite.set();
    }

    public Subject finish() throws ProcessorException{
        if(state == State.DATA) {
            builder.appendData(dataProcessor.finish());
        }
        builder.setTableMode();
        return Suite.set();
    }
}
