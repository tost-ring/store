package app.core.jorg;

import app.core.flow.FlowHashSet;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.WrapSubject;
import app.modules.graph.Graphs;
import app.modules.graph.ReferenceHashGraph;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class JorgWriter {

    private static final Xray nullObject = new Xray(null);
    private Subject objects;
    private GeneralPerformer performer;

    public JorgWriter(GeneralPerformer performer) {
        this.performer = performer;
        objects = new WrapSubject();
    }

    public void addObject(Object object) {
        objects.set(new Xray(object, true));
    }
    public void addObject(Object object, String trace) {
        if(trace.matches("^[\\p{Alpha}_]\\w*$"))
            objects.set(new Xray(object, trace));
        else {
            throw new IllegalArgumentException("Trace pattern is ^[\\p{Alpha}_]\\w*$");
        }
    }

    private ReferenceHashGraph<Xray, String> formReferenceGraph() {
        ReferenceHashGraph<Xray, String> referenceGraph = new ReferenceHashGraph<>();
        referenceGraph.putNode(nullObject);
        Set<Xray> examined = new FlowHashSet<>();
        Set<Xray> toExamine = new HashSet<>(Suite.keys(objects));
        Set<Xray> examining;
        while (toExamine.size() > 0) {
            examining = toExamine;
            toExamine = new HashSet<>();
            for(Xray it : examining) {
                referenceGraph.putNode(it);
                examined.add(it);
            }
            for(Xray it : examining) {
                if(performer.isComplex(it.getObject())) {
                    Subject subject = performer.subjectively(it.getObject());
                    for (String pipe : Suite.keys(subject, String.class)) {
                        Xray dart = referenceGraph.putNode(new Xray(subject.god(pipe, null)));
                        referenceGraph.link(it, pipe, dart);
                        if (performer.isComplex(dart.getObject()) && !examined.contains(dart)) {
                            toExamine.add(dart);
                        }
                    }
                }
            }
        }
        return referenceGraph;
    }

    public boolean write(OutputStream output) {
        ReferenceHashGraph<Xray, String> referenceGraph = formReferenceGraph();

        int id = 1;
        for(Xray it : referenceGraph.getNodes()) {
            if(it.getTrace() == null && (it.isForceTrace() || performer.isComplex(it.getObject()))) {
                it.setTrace("" + id++);
            }
        }
        PrintStream printStream = new PrintStream(output);

        for(Xray it : referenceGraph.getNodes()) {
            if(it.getTrace() != null) {
                printStream.println(encodeHeader(it));
                for(String pipe : referenceGraph.getLinks(it)) {
                    printStream.println(encodeField(pipe, referenceGraph.getNode(it, pipe)));
                }
            }
        }
        try {
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String encodeHeader(Xray xray) {

        String type = performer.getType(xray.getObject());
        if(performer.isComplex(xray.getObject())) {
            return "#" + type + "@" + xray.getTrace();
        } else {
            return "#" + type + "@" + xray.getTrace() + " = " + stringify(xray.getObject());
        }
    }

    public String encodeField(String pipe, Xray dart) {
        String type = performer.getType(dart.getObject());
        if(dart.getTrace() != null) {
            return "\t" + pipe + " = #" + type + "@" + dart.getTrace();
        } else {
            return "\t" + pipe + " = " + stringify(dart.getObject());
        }
    }

    private String stringify(Object object) {
        if(object instanceof String) {
            return "\"" + StringEscapeUtils.escapeJava((String)object) + "\"";
        } else if(object instanceof Integer) {
            return "" + object;
        } else return "";
    }
}
