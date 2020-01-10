package app.core.jorg;

import app.core.suite.Subject;
import app.core.suite.WrapSubject;
import app.modules.graph.Graphs;
import app.modules.graph.ReferenceHashGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JorgReader {

    private final static Pattern idPattern = Pattern.compile("^\\s*#([\\p{Alpha}_]\\w*(?:\\.[\\p{Alpha}_]\\w*)*)(\\[])?@(\\w+)\\s*$");
    private final static Pattern fieldPattern = Pattern.compile("^\\s*(\\w+)\\s*=\\s*(.*)");
    private final static Pattern stringPattern = Pattern.compile("^\\s*\"(.*)\"\\s*$");
    private final static Pattern intPattern = Pattern.compile("^\\s*(\\d+)\\s*$");
    
    private GeneralPerformer performer;
    private Subject objects;

    public JorgReader(GeneralPerformer performer) {
        this.performer = performer;
        objects = new WrapSubject();
    }

    public void read(InputStream inputStream) {
        ReferenceHashGraph<Xkey, String> referenceGraph = new ReferenceHashGraph<>();
        try {
            Xkey xkey = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while(line != null) {
                Matcher matcher = idPattern.matcher(line);
                if(matcher.matches()) {
                    String type = matcher.group(1);
                    boolean arrayType = matcher.group(2) != null;
                    String id = matcher.group(3);
                    xkey = referenceGraph.putNode(new Xkey(type, id, arrayType));
                } else if(xkey != null){
                    matcher = fieldPattern.matcher(line);
                    if(matcher.matches()) {
                        String fieldName = matcher.group(1);
                        String fieldData = matcher.group(2);
                        matcher = idPattern.matcher(fieldData);
                        if(matcher.matches()) {
                            String type = matcher.group(1);
                            boolean arrayType = matcher.group(2) != null;
                            String id = matcher.group(3);
                            referenceGraph.link(xkey, fieldName, new Xkey(type, id, arrayType));
                        } else {
                            matcher = stringPattern.matcher(fieldData);
                            if(matcher.matches()) {
                                String string = matcher.group(1);
                                referenceGraph.link(xkey, fieldName, new Xkey(string, "", null));
                            } else {
                                matcher = intPattern.matcher(fieldData);
                                if(matcher.matches()) {
                                    Integer integer = Integer.parseInt(matcher.group(1));
                                    referenceGraph.link(xkey, fieldName, new Xkey(integer, "", null));
                                }
                            }
                        }
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

    private Subject nodeAdjacentAsSubject(ReferenceHashGraph<Xkey, String> referenceGraph, Xkey node) {
        Subject subject = new WrapSubject();
        for(String link : referenceGraph.getLinks(node)) {
            subject.set(link, referenceGraph.getNode(node, link).getObject());
        }
        return subject;
    }

    public Subject getObjects() {
        return objects;
    }
}
