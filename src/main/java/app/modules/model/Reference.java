package app.modules.model;

public class Reference {

    private String subject;

    public Reference(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public int hashCode() {
        return subject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Reference && subject.equals(((Reference) obj).subject);
    }
}
