package app.modules.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class XMLWriter {
    public static String xmlSettings = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private File file;
    private Set<String> imports;
    private StringBuilder nodeTree;

    public XMLWriter(File file){
         this.file = file;
    }

    public XMLWriter(String filePath){
        this.file = new File(filePath);
    }

    public static void write(XMLNodeAdapter adapter, File file)throws FileNotFoundException {
        new XMLWriter(file).write(adapter);
    }

    public void write(XMLNodeAdapter adapter) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        imports = new HashSet<>();
        nodeTree = new StringBuilder();

        adapter.toXmlNode().appendTo(nodeTree, "");

        write(nodeTree.toString(), out);
    }

    public void writeChildren(XMLNodeAdapter adapter) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        imports = new HashSet<>();
        nodeTree = new StringBuilder();

        adapter.toXmlNode().getChildren().forEach(x -> x.toXmlNode().appendTo(nodeTree, ""));

        write(nodeTree.toString(), out);
    }

    private void write(String str, PrintWriter out) {
        out.println(xmlSettings);
        out.println();
        for(String it : imports){
            out.println(it);
        }
        out.println();
        out.println(str);
        out.close();
    }
}
