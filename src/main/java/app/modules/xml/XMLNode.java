package app.modules.xml;

import java.util.*;
import java.util.stream.Collectors;

public class XMLNode implements XMLNodeAdapter {
    private String name;
    private Map<String, String> attributes;
    private List<XMLNodeAdapter> children;

    public XMLNode(String name) {
        this.name = name;
        this.attributes = new HashMap<>();
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void set(String attributeName, String attributeValue){
        attributes.put(attributeName,attributeValue);
    }

    public String god(String attributeName, String substitute){
        return attributes.getOrDefault(attributeName, substitute);
    }

    public String get(String attributeName) throws XMLParsingException {
        String attribute = attributes.get(attributeName);
        if(attribute == null)throw new XMLParsingException("Missing '" + attributeName + "' in '" + getName() + "'");
        return attribute;
    }

    public boolean is(String attributeName) {
        return attributes.containsKey(attributeName);
    }

    public List<XMLNodeAdapter> getChildren() {
        return children;
    }

    public void appendTo(StringBuilder str, String prefix){
        str.append(prefix).append("<").append(name);
        for(String it : attributes.keySet()){
            str.append(" ").append(it).append("=\"")
                    .append(attributes.get(it)).append("\"");
        }
        List<XMLNode> nodes = children.stream().map(XMLNodeAdapter::toXmlNode).filter(Objects::nonNull).collect(Collectors.toList());
        if(nodes.isEmpty()){
            str.append("/>\n");
        } else {
            str.append(">\n");
            nodes.forEach(x -> x.appendTo(str, prefix + "\t"));
            str.append(prefix).append("</").append(name).append(">\n");
        }
    }

    @Override
    public XMLNode toXmlNode() {
        return this;
    }
}
