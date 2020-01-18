package app.modules.xml;

import app.core.suite.*;

import java.util.Comparator;

public class Encoder {

    private Subject refs = new WrapSubject();

    static class Reference {
        Object referred;
        int index = -1;
        String header;
        Subject data;

        Reference(Object referred) {
            this.referred = referred;
        }

        @Override
        public int hashCode() {
            return referred.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Reference && this.referred == ((Reference) obj).referred;
        }

        @Override
        public String toString() {
            return "#" + index;
        }
    }

    public Reference put(Object o) {

        Reference reference = new Reference(o);
        reference = refs.goc(reference, reference);
        if(reference.index < 0) {
            reference.index = Suite.size(refs) - 1;
            reference.header = header(o);
            Subject subject = subjectize(o);
            if(subject != null) {
                Suite.keys(subject, Object.class).mapTo(o1 -> subject.set(o1, put(subject.get(o1))));
                reference.data = subject;
            }
        }

        return reference;
    }

    private String header(Object o) {
        if(o instanceof Subjective) {
            return "";
        } else return o.toString();
    }

    private Subject subjectize(Object o) {
        if(o instanceof Subjective) {
            return ((Subjective) o).toSubject();
        } else return null;
    }

    public String encode() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Reference reference : Suite.keys(refs, Reference.class).sortedBy(Comparator.comparingInt(o -> o.index))) {
            stringBuilder.append(reference).append(" = ").append(reference.header);
            if(reference.data != null) {
                stringBuilder.append(encodeSubject(reference.data));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private String encodeSubject(Subject subject) {
        StringBuilder stringBuilder = new StringBuilder("{");
        for(Object o : Suite.keys(subject, Object.class)) {
            stringBuilder.append("\n\t").append(o).append(" = ").append(subject.getAs(o, Object.class));
        }
        stringBuilder.append(Suite.size(subject) > 0 ? "\n\t}" : "}");
        return stringBuilder.toString();
    }

}
