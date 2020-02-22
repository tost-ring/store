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

    private final static Pattern quillPattern =
            Pattern.compile("^\\s*@(\\w+)\\s*(?:#([\\p{Alpha}_]\\w*(?:\\.[\\p{Alpha}_]\\w*)*(?:\\$[\\p{Alpha}_]\\w*)?))?(\\[])?\\s*(?:\\[\\s*(.*)\\s*)?$");
    private final static Pattern referencePattern = Pattern.compile("^\\s*@(\\w+)\\s*$");
    private final static Pattern arrowPattern = Pattern.compile("^\\s*\\[\\s*(.*)\\s*]\\s*(.*)");
    private static final Pattern humbleStringPattern = Pattern.compile("^\\s*([\\p{Alpha}_]\\w*)\\s*$");
    private final static Pattern stringPattern = Pattern.compile("^\\s*\"(.*)\"\\s*$");
    private final static Pattern intPattern = Pattern.compile("^\\s*(\\d+)\\s*$");
    private final static Pattern classPattern = Pattern.compile("^\\s*#([\\p{Alpha}_]\\w*(?:\\.[\\p{Alpha}_]\\w*)*(?:\\$[\\p{Alpha}_]\\w*)?)\\s*$");
    private final static Pattern fieldPattern = Pattern.compile("^\\s*([\\p{Alpha}_]\\w*)\\s*$");

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
            int lineCounter = 1;
            String line = reader.readLine();
            while(line != null) {
                if(!line.isBlank()) {
                    Xkey id = parseQuill(line);
                    if (id != null) {
                        quill = referenceGraph.putNode(id);
                        if (quill != id) {
                            quill.set(id);
                        }
                    } else if (quill != null) {
                        Subject parseResult = parseArrow(line);
                        if (parseResult.are("pipe", "dart")) {
                            Xkey pipe = referenceGraph.putNode(parseResult.get("pipe"));
                            Xkey dart = referenceGraph.putNode(parseResult.get("dart"));
                            referenceGraph.link(quill, pipe, dart);
                        } else throw new JorgReadException("Invalid syntax at line " + lineCounter + ": " + line);
                    } else throw new JorgReadException("Invalid syntax at line " + lineCounter + ": " + line);
                }
                line = reader.readLine();
                ++lineCounter;
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
//        System.out.println(Graphs.toString(referenceGraph));
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
        Xkey xkey = parseReference(string);
        if(xkey != null)return xkey;
        xkey = parseString(string);
        if(xkey != null)return xkey;
        xkey = parseInteger(string);
        if(xkey != null)return xkey;
        xkey = parseField(string);
        if(xkey != null)return xkey;
        xkey = parseClass(string);
        return xkey;
    }

    private Xkey parseQuill(String string) {
        Matcher matcher = quillPattern.matcher(string);
        if(matcher.matches()) {
            String id = matcher.group(1);
            String type = matcher.group(2);
            if(type == null) {
                type = "Subject";
            }
            boolean arrayType = matcher.group(3) != null;
            return new Xkey(type, id, arrayType);
        } else return null;
    }

    private Xkey parseReference(String string) {
        Matcher matcher = referencePattern.matcher(string);
        if(matcher.matches()) {
            String id = matcher.group(1);
            return new Xkey(null, "", id);
        } else return null;
    }

    private Xkey parseString(String string) {
        Matcher matcher = humbleStringPattern.matcher(string);
        if (matcher.matches()) {
            String str = matcher.group(1);
            return new Xkey(str, "", null);
        }
        matcher = stringPattern.matcher(string);
        if (matcher.matches()) {
            String str = StringEscapeUtils.unescapeJava(matcher.group(1));
            return new Xkey(str, "", null);
        }
        return null;
    }

    private Xkey parseInteger(String string) {
        Matcher matcher = intPattern.matcher(string);
        if (matcher.matches()) {
            Integer integer = Integer.parseInt(matcher.group(1));
            return new Xkey(integer, "", null);
        } else return null;
    }

    private Xkey parseField(String string) {
        Matcher matcher = fieldPattern.matcher(string);
        if (matcher.matches()) {
            String str = matcher.group(1);
            return new Xkey(str, "", null);
        } else return null;
    }

    private Xkey parseClass(String string) {
        Matcher matcher = classPattern.matcher(string);
        if (matcher.matches()) {
            Class<?> type = performer.getType(matcher.group(1));
            System.out.println(type);
            return new Xkey(type, "", null);
        } else return null;
    }

     private Subject parseArrow(String line) {
         Matcher matcher = arrowPattern.matcher(line);
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
