package app.core.jorg;

import app.core.fluid.Cascade;
import app.core.fluid.Fluid;
import app.core.fluid.FluidIterator;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.processor.JorgProcessor;
import app.modules.model.Port;
import app.modules.model.TablePort;
import app.modules.model.processor.ProcessorException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class JorgWriter {

    private static final int escapeCharacter = '`';

    public static boolean write(Object object, String filePath) {
        JorgWriter writer = new JorgWriter();
        writer.objects.set("0", object);
        try {
            writer.save(new FileOutputStream(filePath));
        } catch (JorgWriteException | IOException e) {
            return false;
        }
        return true;
    }

    public static boolean write(Object object, File file) {
        JorgWriter writer = new JorgWriter();
        writer.objects.set("0", object);
        try {
            writer.save(new FileOutputStream(file));
        } catch (JorgWriteException | IOException e) {
            return false;
        }
        return true;
    }

    public static boolean write(Object object, URL url) {
        JorgWriter writer = new JorgWriter();
        writer.objects.set("0", object);
        try {
            URLConnection connection = url.openConnection();
            writer.save(connection.getOutputStream());
        } catch (IOException | JorgWriteException e) {
            return false;
        }
        return true;
    }

    public static String encode(Object o) {
        JorgWriter writer = new JorgWriter();
        writer.objects.set("0", o);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return writer.saveWell(outputStream) ? outputStream.toString() : "";
    }

    private final Subject objects;
    private final JorgPerformer performer;
    private boolean compactMode;

    public JorgWriter() {
        this(new JorgPerformer());
    }

    public JorgWriter(JorgPerformer performer) {
        this.performer = performer;
        objects = Suite.set();
        compactMode = false;
    }

    public boolean isCompactMode() {
        return compactMode;
    }

    public void setCompactMode(boolean compactMode) {
        this.compactMode = compactMode;
    }

    public void addObject(String id, Object object) {
        if(id.matches("^\\w")) {
            objects.set(id, object);
        } else {
            throw new IllegalArgumentException("Trace pattern is ^\\w");
        }
    }

    public boolean saveWell(File file) {
        try {
            save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(File file) throws IOException, JorgWriteException {
        save(new FileOutputStream(file));
    }

    public boolean saveWell(URL url) {
        try {
            save(url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(URL url) throws IOException, JorgWriteException {
        URLConnection connection = url.openConnection();
        save(connection.getOutputStream());
    }

    public boolean saveWell(OutputStream output) {
        try {
            save(output);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(OutputStream output) throws JorgWriteException, IOException {

        OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);

        Cascade<Crystal> crystals = performer.melt(objects);

        for(Crystal c : crystals.toEnd()) {
            if(crystals.getFalls() > 1 || !"0".equals(c.getId())) {
                writer.write(compactMode ? "@[" : "@ [ ");
                writer.write(c.getId());
                writer.write(compactMode ? "]" : " ] ");
            }
            Cascade<Subject> cascade = c.getBody().front().cascade();

            for(var ref : cascade.until(s -> s.key().asGiven(Crystal.class).getGerm() instanceof Suite.Add)) {
                if(cascade.getFalls() > 1) {
                    writer.write(compactMode ? "]" : " ] ");
                }
                Crystal c1 = ref.asExpected();
                if(c1.getId() == null) {
                    writer.write(stringify(c1.getGerm()));
                } else {
                    writer.write("@");
                    writer.write(escapedHumble(c1.getId(), false));
                }
            }
            for(var ref : cascade.toEnd()) {
                Crystal c1 = ref.key().asExpected();
                writer.write(compactMode ? "[" : "\n [ ");
                if(c1.getId() == null) {
                    writer.write(stringify(c1.getGerm()));
                } else {
                    writer.write("@");
                    writer.write(escapedHumble(c1.getId(), false));
                }
                writer.write(compactMode ? "]" : " ] ");

                Crystal c2 = ref.asExpected();
                if(c2.getId() == null) {
                    writer.write(stringify(c2.getGerm()));
                } else {
                    writer.write("@");
                    writer.write(escapedHumble(c2.getId(), false));
                }
            }
            if(!compactMode)writer.write("\n\n");
        }
        writer.flush();
        output.close();
    }

    private String stringify(Object object) throws JorgWriteException {
        if(object instanceof String) {
            return escaped((String)object);
        } else if(object instanceof Integer) {
            return "" + object;
        } else if(object instanceof Double) {
            return "" + object;
        } else if(object instanceof Port) {
            if (object instanceof TablePort) {
                return "#[" + ((TablePort) object).getSize() + "]" + escapedHumble(((TablePort) object).getLabel(), false);
            } else {
                return "#" + escapedHumble(((Port) object).getLabel(), false);
            }
        } else if(object == null || object instanceof Suite.Add) {
            return "";
        } else {
            throw new JorgWriteException("Unrecognized object type " + object.getClass());
        }
    }

    private String escaped(String str) {
        String humble = escapedHumble(str, true);
        int humbleLength = humble.length();
        if(humbleLength > 0) {
            if(humbleLength <= str.length() + 2) {
                return humble;
            }
            StringBuilder stringBuilder = new StringBuilder("\"");
            str.chars().forEach(cp -> {
                if(cp == '"' || JorgProcessor.isEscapeCharacter(cp)) {
                    stringBuilder.appendCodePoint(escapeCharacter);
                }
                stringBuilder.appendCodePoint(cp);
            });
            stringBuilder.append('"');
            String quoted = stringBuilder.toString();
            return humbleLength < quoted.length() ? humble : quoted;

        } else return "\"\"";
    }

    private String escapedHumble(String str, boolean escapeNonHumbleEntry) {
        if(str == null || str.isEmpty())return "";
        StringBuilder stringBuilder = new StringBuilder(str.length());
        if(escapeNonHumbleEntry) {
            int startCodePoint = str.codePointAt(0);
            if(!Character.isJavaIdentifierStart(startCodePoint) &&
                    !JorgProcessor.isControlCharacter(startCodePoint) &&
                    !JorgProcessor.isEscapeCharacter(startCodePoint)) {
                stringBuilder.appendCodePoint(escapeCharacter);
            }
        }
        AtomicReference<StringBuilder> endSpaces = new AtomicReference<>(new StringBuilder());
        str.chars().forEach(cp -> {
            if(Character.isWhitespace(cp)) {
                endSpaces.get().appendCodePoint(cp);
            } else {
                if(endSpaces.get().length() > 0) {
                    endSpaces.get().chars().forEach(ws -> stringBuilder.appendCodePoint(escapeCharacter).appendCodePoint(ws));
                    endSpaces.set(new StringBuilder());
                }
                if (JorgProcessor.isControlCharacter(cp) || JorgProcessor.isEscapeCharacter(cp)) {
                    stringBuilder.appendCodePoint(escapeCharacter);
                }
                stringBuilder.appendCodePoint(cp);
            }
        });
        return stringBuilder.toString();
    }

}
