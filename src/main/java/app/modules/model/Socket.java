package app.modules.model;

public class Socket {

    private String subject;

    public Socket(String subject) {
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
        return obj instanceof Socket && subject.equals(((Socket) obj).subject);
    }

    @Override
    public String toString() {
        return "#" + subject;
    }
}
