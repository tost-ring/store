package app.modules.xml;

public class XMLParsingException extends Exception {
    public XMLParsingException() {
    }

    public XMLParsingException(String message) {
        super(message);
    }

    public XMLParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLParsingException(Throwable cause) {
        super(cause);
    }

    public XMLParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
