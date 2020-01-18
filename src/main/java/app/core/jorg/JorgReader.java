package app.core.jorg;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.WrapSubject;
import app.modules.graph.Graphs;
import app.modules.graph.ReferenceHashGraph;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JorgReader {

    private final static Pattern idPattern = Pattern.compile("^\\s*#([\\p{Alpha}_]\\w*(?:\\.[\\p{Alpha}_]\\w*)*)(\\[])?@(\\w+)\\s*$");
    private final static Pattern fieldPattern = Pattern.compile("^\\s*\\[\\s*(.*)\\s*]\\s*(.*)");
    private final static Pattern stringPattern = Pattern.compile("^\\s*\"(.*)\"\\s*$");
    private final static Pattern intPattern = Pattern.compile("^\\s*(\\d+)\\s*$");

    public static<T> T read(String filePath) {
        JorgReader reader = new JorgReader();
        try {
            reader.read(new FileInputStream(filePath));
            return reader.getObjects().god("o", null);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private GeneralPerformer performer;
    private Subject objects;

    public JorgReader() {
        this(new GeneralPerformer(Suite.
                set("Subject", Subject.class)));
    }

    public JorgReader(GeneralPerformer performer) {
        this.performer = performer;
        objects = new WrapSubject();
    }

    public void read(InputStream inputStream) {
        ReferenceHashGraph<Xkey, Xkey> referenceGraph = new ReferenceHashGraph<>();
        try {
            Xkey quill = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while(line != null) {
                Xkey id = parseId(line);
                if(id != null) {
                    quill = referenceGraph.putNode(id);
                } else if(quill != null) {
                    Subject parseResult = parseField(line);
                    if(parseResult.are("pipe", "dart")) {
                        Xkey pipe = referenceGraph.putNode(parseResult.get("pipe"));
                        Xkey dart = referenceGraph.putNode(parseResult.get("dart"));
                        referenceGraph.link(quill, pipe, dart);
                    }
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Xkey it : referenceGraph.getNodes()) {
            if(it.getObject() == null) {
                Object o;
                if(it.isArray() || performer.isArrayType(it.getType())) {
                    o = performer.formArray(it.getType(), referenceGraph.countNodes(it));
                } else {
                    o = performer.formObject(it.getType());
                }
                it.setObject(o);
            }
        }
        for(Xkey it : referenceGraph.getNodes()) {
            if(performer.isComplex(it.getObject())) {
                Subject adjacent = nodeAdjacentAsSubject(referenceGraph, it);
                performer.objectively(it.getObject(), adjacent);
            }
            if(it.getId() != null) {
                objects.set(it.getId(), it.getObject());
            }
        }
    }

    private Xkey parse(String string) {
        Xkey xkey = parseId(string);
        if(xkey != null)return xkey;
        xkey = parseString(string);
        if(xkey != null)return xkey;
        xkey = parseInteger(string);
        return xkey;
    }

    private Xkey parseId(String string) {
        Matcher matcher = idPattern.matcher(string);
        if(matcher.matches()) {
            String type = matcher.group(1);
            boolean arrayType = matcher.group(2) != null;
            String id = matcher.group(3);
            return new Xkey(type, id, arrayType);
        } else return null;
    }

    private Xkey parseString(String string) {
        Matcher matcher = stringPattern.matcher(string);
        if (matcher.matches()) {
            String str = StringEscapeUtils.unescapeJava(matcher.group(1));
            return new Xkey(str, "", null);
        } else return null;
    }

    private Xkey parseInteger(String string) {
        Matcher matcher = intPattern.matcher(string);
        if (matcher.matches()) {
            Integer integer = Integer.parseInt(matcher.group(1));
            return new Xkey(integer, "", null);
        } else return null;
    }

     private Subject parseField(String line) {
         Matcher matcher = fieldPattern.matcher(line);
         if (matcher.matches()) {
             Xkey pipe = parse(matcher.group(1));
             Xkey dart = parse(matcher.group(2));
             return Suite.set("pipe", pipe).set("dart", dart);
         }
         return Suite.set();
     }

    private Subject nodeAdjacentAsSubject(ReferenceHashGraph<Xkey, Xkey> referenceGraph, Xkey node) {
        Subject subject = Suite.set();
        for(Xkey pipe : referenceGraph.getLinks(node)) {
            subject.sos(pipe.getObject(), referenceGraph.getNode(node, pipe).getObject());
        }
        return subject;
    }

    public Subject getObjects() {
        return objects;
    }
}
