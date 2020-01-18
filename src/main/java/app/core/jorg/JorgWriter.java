package app.core.jorg;

import app.core.flow.FlowHashSet;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.WrapSubject;
import app.modules.graph.ReferenceHashGraph;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class JorgWriter {

    private static final Xray nullObject = new Xray(null);

    public static boolean write(Object object, String filePath) {
        JorgWriter writer = new JorgWriter();
        writer.addObject(object, "o");
        try {
            return writer.write(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    private Subject objects;
    private GeneralPerformer performer;

    public JorgWriter() {
        this(new GeneralPerformer(Suite.
                set(WrapSubject.class, "Subject")));
    }

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

    private ReferenceHashGraph<Xray, Xray> formReferenceGraph() {
        ReferenceHashGraph<Xray, Xray> referenceGraph = new ReferenceHashGraph<>();
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
                    for (Object key : Suite.keys(subject, String.class)) {
                        Xray pipe = referenceGraph.putNode(new Xray(key));
                        referenceGraph.putNode(pipe);
                        if (performer.isComplex(pipe.getObject()) && !examined.contains(pipe)) {
                            toExamine.add(pipe);
                        }
                        Xray dart = referenceGraph.putNode(new Xray(subject.god(key, null)));
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
        ReferenceHashGraph<Xray, Xray> referenceGraph = formReferenceGraph();

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
                for(Xray pipe : referenceGraph.getLinks(it)) {
                    printStream.println(encodeField(pipe, referenceGraph.getNode(it, pipe)));
                }
                printStream.println();
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

        String type = performer.getTypeName(xray.getObject());
        if(performer.isComplex(xray.getObject())) {
            return stringify(xray);
        } else {
            return stringify(xray) + " = " + stringify(xray.getObject());
        }
    }

    public String encodeField(Xray pipe, Xray dart) {
        return "\t[ " + stringify(pipe) + " ] " + stringify(dart);
    }

    private String stringify(Xray xray) {
        if(xray.getTrace() != null) {
            return "#" + performer.getTypeName(xray.getObject()) + "@" + xray.getTrace();
        } else {
            return stringify(xray.getObject());
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
