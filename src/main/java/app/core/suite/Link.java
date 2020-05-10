package app.core.suite;

class Link {
    Link front;
    Link back;
    Subject subject;

    Link() {
        this.subject = ZeroSubject.getInstance();
        front = this;
        back = this;
    }

    Link(Subject subject) {
        this.subject = subject;
        front = this;
        back = this;
    }

    Link(Link front, Link back, Subject subject) {
        this.subject = subject;
        this.front = front;
        this.back = back;
    }

    Link(Link front, Link back, Object key, Object value) {
        subject = new CoupleSubject(key, value);
        this.front = front;
        this.back = back;
    }

    void setValue(Object value) {
        subject = subject.set(subject.key().direct(), value);
    }
}
