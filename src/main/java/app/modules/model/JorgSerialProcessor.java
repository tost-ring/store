package app.modules.model;

import app.core.jorg.Xkey;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.graph.ReferenceHashGraph;

import java.util.ArrayList;
import java.util.List;

public class JorgSerialProcessor{

    private int counter;
    private ReferenceHashGraph<Xkey, Xkey> referenceGraph;
    private boolean homeFlag;
    private boolean idFlag;
    private StringBuilder builder;
    private String id;
    private String type;
    private List<String> list;


    public Subject ready() {
        counter = 0;
        referenceGraph = new ReferenceHashGraph<>();
        homeFlag = true;
        idFlag = false;
        return Suite.set();
    }

    public Subject advance(int i) throws ProcessorException{
        ++counter;
        if(homeFlag) {
            if(isBlankCodePoint(i)) {

            } else if(i == '@') {
                builder = new StringBuilder();
                list = new ArrayList<>();
                homeFlag = false;
                idFlag = true;
            } else {
                throw new ProcessorException("Illegal character " + counter);
            }
        } else {
            if(idFlag) {
                if(Character.isLetterOrDigit(i)) {
                    builder.appendCodePoint(i);
                } else if(i == '#') {

                }
            }
        }
        return Suite.set();
    }

    public Subject finish() {
        return null;
    }

    private boolean isBlankCodePoint(int i) {
        return i == ' ' || i == '\t' || i == '\n' || i == '\r';
    }
}
