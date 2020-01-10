package app.modules.model.items;

import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import app.core.flow.FlowHashSet;
import app.core.suite.WrapSubject;
import app.modules.graph.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exporter {

    class Xray {
        final Object o;
        String id;
        boolean basic;

        Xray(Object obj) {
            this(obj, false);
        }

        Xray(Object obj, boolean basic) {
            o = obj;
            this.basic = basic;
        }

        @Override
        public boolean equals(Object o1) {
            return (o1 instanceof Xray && o == ((Xray) o1).o) || o == o1;
        }

        @Override
        public int hashCode() {
            return Objects.hash(o);
        }

        public Subject analyze() {
            if(o instanceof Subjective){
                return ((Subjective) o).toSubject();
            } else return Suite.set();
        }

        public String encode() {
            if(o instanceof String) {
                return "\"" + StringEscapeUtils.escapeJava(o.toString()) + "\"";
            } else if(o instanceof Integer || o instanceof Double || o instanceof Character) {
                return o.toString();
            } else {
                return encodeId();
            }
        }

        public boolean isComposite() {
            return !(o == null || o instanceof String || o instanceof Integer ||
                    o instanceof Double || o instanceof Character);
        }

        public String encodeId() {
            return (o == null ? "" : namingGraph.getBlack(o.getClass())) + "@" + id;
        }

        @Override
        public String toString() {
            return encode();
        }
    }

    private DupleGraph<String, Class<?>> namingGraph;
    private Set<Xray> objects;
    private Xray nullObject = new Xray(null);

    public Exporter(Subject naming) {
        objects = new HashSet<>();
        namingGraph = new HashDupleGraph<>();
        for(String it : Suite.keys(naming, String.class)) {
            namingGraph.link(it, naming.get(it));
        }

    }

    public void addObject(Object object) {
        objects.add(new Xray(object, true));
    }

    public String export() {
        ReferenceHashGraph<Xray, String> references = new ReferenceHashGraph<>();
        references.putNode(nullObject);
        Set<Xray> analised = new FlowHashSet<>(nullObject);
        Set<Xray> toAnalise = new HashSet<>(objects);
        Set<Xray> analising;
        while (toAnalise.size() > 0) {
            analising = toAnalise;
            toAnalise = new HashSet<>();
            for(Xray it : analising) {
                references.putNode(it);
                analised.add(it);
            }
            for(Xray it : analising) {
                Subject analyse = it.analyze();
                for(String pipe : Suite.keys(analyse, String.class)) {
                    Xray dart = references.putNode(new Xray(analyse.god(pipe, null)));
                    references.link(it, pipe, dart);
                    if(dart.isComposite() && !analised.contains(dart)) {
                        toAnalise.add(dart);
                    }
                }
            }
        }
        int id = 1;
        for(Xray it : references.getNodes()) {
            if(it.isComposite()) {
                it.id = "" + id++;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Xray it : references.getNodes()) {
            Collection<String> refs = references.getLinks(it);
            if(refs.isEmpty()) {
                if(it.basic) {
                    stringBuilder.append(it.encodeId()).append(" = ").append(it.encode()).append("\n");
                }
            } else {
                stringBuilder.append(it.encode()).append("\n");
                for(String ref : refs) {
                    stringBuilder.append("\t").append(ref).append(" = ").append(references.getNode(it, ref).encode()).append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    static class Xkey {
        Object o;
        String type;
        String id;

        public Xkey(String type, String id) {
            this.type = type;
            this.id = id;
        }

        public Xkey(Object o, String type, String id) {
            this.o = o;
            this.type = type;
            this.id = id;
        }

        @Override
        public boolean equals(Object o1) {
            return o1 instanceof Xkey && !id.isEmpty() && id.equals(((Xkey) o1).id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        public void synthesize(Subject subject) {
            if(o instanceof Subjective){
                ((Subjective) o).fromSubject(subject);
            }
        }

        @Override
        public String toString() {
            if(o instanceof String) {
                return "\"" + o + "\"";
            } else if(o instanceof Integer) {
                return "" + o;
            } else {
                return type + "@" + id;
            }
        }
    }

    private final static Pattern idPattern = Pattern.compile("^\\s*([\\p{Alpha}_]\\w*)\\s*@(\\w+)\\s*$");
    private final static Pattern fieldPattern = Pattern.compile("^\\s*([\\p{Alpha}_]\\w*)\\s*=\\s*(.*)");
    private final static Pattern stringPattern = Pattern.compile("^\\s*\"(.*)\"\\s*$");
    private final static Pattern intPattern = Pattern.compile("^\\s*(\\d+)\\s*$");

    public void _import(InputStream inputStream) {
        ReferenceHashGraph<Xkey, String> references = new ReferenceHashGraph<>();
        try {
            Xkey xkey = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while(line != null) {
                Matcher matcher = idPattern.matcher(line);
                if(matcher.matches()) {
                    String type = matcher.group(1);
                    String id = matcher.group(2);
                    xkey = references.putNode(new Xkey(type, id));
                } else if(xkey != null){
                    matcher = fieldPattern.matcher(line);
                    if(matcher.matches()) {
                        String fieldName = matcher.group(1);
                        String fieldData = matcher.group(2);
                        matcher = idPattern.matcher(fieldData);
                        if(matcher.matches()) {
                            String type = matcher.group(1);
                            String id = matcher.group(2);
                            references.link(xkey, fieldName, new Xkey(type, id));
                        } else {
                            matcher = stringPattern.matcher(fieldData);
                            if(matcher.matches()) {
                                String string = matcher.group(1);
                                references.link(xkey, fieldName, new Xkey(string, "", ""));
                            } else {
                                matcher = intPattern.matcher(fieldData);
                                if(matcher.matches()) {
                                    Integer integer = Integer.parseInt(matcher.group(1));
                                    references.link(xkey, fieldName, new Xkey(integer, "", ""));
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
        try {
            for(Xkey it : references.getNodes()) {
                if(it.o == null) {
                    Class<?> type = namingGraph.getWhite(it.type);
                    if(type != null) {
                        it.o = type.getConstructor().newInstance();
                    }
                }
            }
            for(Xkey it : references.getNodes()) {
                if(it.o instanceof Subjective) {
                    Subject adjacent = nodeAdjacentAsSubject(references, it);
                    ((Subjective) it.o).fromSubject(adjacent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Graphs.toString(references));
    }

    private<N,L> Subject nodeAdjacentAsSubject(ReferenceHashGraph<N, L> referenceGraph, N node) {
        Subject subject = new WrapSubject();
        for(L link : referenceGraph.getLinks(node)) {
            subject.set(link, referenceGraph.getNode(node, link));
        }
        return subject;
    }
}
