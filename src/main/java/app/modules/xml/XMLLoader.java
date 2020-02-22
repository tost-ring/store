package app.modules.xml;

import app.core.suite.Subject;
import app.core.suite.WrapSubject;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class XMLLoader {

    private static final Object xmlName = new Object();

    private File file;

    public XMLLoader(File file){
        this.file = file;
    }

    public XMLLoader(String filePath){
        this.file = new File(filePath);
    }

    public static Subject load(File file)throws FileNotFoundException, XMLStreamException {

        return new XMLLoader(file).load();
    }

    public Subject load() throws XMLStreamException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);

        Stack<Subject> parents = new Stack<>();
        parents.push(new WrapSubject());

        XMLEventReader eventReader = XMLInputFactory.newInstance().
                createXMLEventReader(inputStream);
        Subject subject;

        while(eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            switch (event.getEventType()) {

                case XMLStreamConstants.START_ELEMENT:
                    StartElement element = event.asStartElement();
                    subject = new WrapSubject();

                    for (Iterator<Attribute> iter = element.getAttributes(); iter.hasNext(); ) {
                        Attribute attribute = iter.next();
                        subject.set(attribute.getName().getLocalPart(), attribute.getValue());
                    }
                    parents.push(subject);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    subject = parents.pop();
                    parents.peek().set(endElement.getName().getLocalPart(), subject);
                    break;
            }
        }
        return parents.pop();
    }

}
