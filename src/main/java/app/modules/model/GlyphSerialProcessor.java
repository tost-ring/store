package app.modules.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class GlyphSerialProcessor extends TextSerialProcessor {

    private boolean standardize;
    private Queue<String> strings;
    private StringBuilder builder;

    public GlyphSerialProcessor(boolean standardize) {
        this.standardize = standardize;

        Node inQuote = new Node();

        connect(getStartNode(), i -> i == '"', this::pushBuilder, inQuote);

        connect(inQuote, i -> i == '"', this::pushBuilder, getStartNode());
        connect(inQuote, i -> true, this::pushCharacterQuoted, inQuote);
        connect(inQuote, i -> false, this::pushBuilder, getEndNode());

        connect(getStartNode(), Character::isWhitespace, this::pushBuilder, getStartNode());
        connect(getStartNode(), i -> true, this::pushCharacter, getStartNode());
        connect(getStartNode(), i -> false, this::pushBuilder, getEndNode());
    }

    @Override
    public void start() {
        super.start();
        strings = new LinkedList<>();
        builder = new StringBuilder();
    }

    private void pushBuilder(int ch) {
        System.out.println("pushB");
        if(builder.length() > 0) {
            strings.add(builder.toString());
            builder = new StringBuilder();
        }
    }

    private void pushCharacter(int ch) {
        System.out.println("pushCh " + ch + " " + Character.toString(ch));
        if(standardize) {
            builder.appendCodePoint(Character.toLowerCase(ch));
        } else {
            builder.appendCodePoint(ch);
        }
    }

    private void pushCharacterQuoted(int ch) {
        if(standardize) {
            builder.appendCodePoint(Character.toLowerCase(ch));
        } else {
            builder.appendCodePoint(ch);
        }
    }

    public Queue<String> getStrings() {
        return strings;
    }
}
