package app.core.jorg;

import app.core.flow.FlowHashSet;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.WrapSubject;
import app.modules.graph.Graphs;
import app.modules.graph.ReferenceHashGraph;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JorgWriter {

    private static final Xray nullObject = new Xray(null);
    private static final Pattern humbleString = Pattern.compile("^[\\p{Alpha}_]\\w*$");

    public static boolean write(Object object, String filePath) {
        JorgWriter writer = new JorgWriter();
        writer.objects.set(new Xray(object, "0"));
        try {
            return writer.write(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static boolean write(Object object, File file) {
        JorgWriter writer = new JorgWriter();
        writer.objects.set(new Xray(object, "0"));
        try {
            return writer.write(new FileOutputStream(file));
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
        if(humbleString.matcher(trace).matches())
            objects.set(new Xray(object, trace));
        else {
            throw new IllegalArgumentException("Trace pattern is " + humbleString.pattern());
        }
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

        try {
            for(Xray it : referenceGraph.getNodes()) {
                if(it.getTrace() != null) {
                    printStream.println(encodeHeader(it));
                    for(Xray pipe : referenceGraph.getLinks(it)) {
                        printStream.println(encodeField(pipe, referenceGraph.getNode(it, pipe)));
                    }
                    printStream.println();
                }
            }
            output.close();
            return true;
        } catch (IOException | JorgWriteException e) {
            e.printStackTrace();
        }
        return false;
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
                    for (Object key : subject.keys()) {
                        Xray pipe = referenceGraph.putNode(new Xray(key));
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

    private String encodeHeader(Xray xray) throws JorgWriteException {

        Object object = xray.getObject();
        if(performer.isPrimitive(object)) {
            return stringify(xray) + " ] " + stringify(object);
        } else if(object instanceof Subject) {
            return stringify(xray);
        } else {
            String typeName = performer.getTypeAlias(object);
            return stringify(xray) + " #" + typeName;
        }

    }

    private String encodeField(Xray pipe, Xray dart) throws JorgWriteException {
        return "  [ " + stringify(pipe) + " ] " + stringify(dart);
    }

    private String stringify(Xray xray) throws JorgWriteException {
        if(xray.getTrace() == null) return stringify(xray.getObject());
        return "@" + xray.getTrace();
    }

    private String stringify(Object object) throws JorgWriteException {
        if(object instanceof String) {
            String str = (String)object;
            return isHumbleString(str) ? str : "\"" + StringEscapeUtils.escapeJava(str) + "\"";
        } else if(object instanceof Integer) {
            return "" + object;
        } else if(object instanceof Double) {
            return "" + object;
        } else if(object instanceof Class) {
            return "#" + performer.getAlias((Class<?>)object);
        } else if(object instanceof Field) {
            return ((Field) object).getName();
        } else if(object == null) {
            return "?";
        } else {
            throw new JorgWriteException("Unrecognized object type " + object.getClass());
        }
    }

    boolean isHumbleString(String str) {
        return humbleString.matcher(str).matches();
    }
}
