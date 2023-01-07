package converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Element {
    String path;
    String value;
    Map<String, String> attributes;
    List<Element> nestedElementsList = new ArrayList<>();
    Element parent;
    boolean hasChildren;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<Element> getNestedElementsList() {
        return nestedElementsList;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        if (nestedElementsList == null || nestedElementsList.isEmpty()) {
            return "Element:\n" +
                    "path = " + path + "\n" +
                    (!hasChildren ? ("value = " + value + "\n") : "")
                    +
                    ((attributes != null && !attributes.isEmpty()) ? ("attributes:\n" + printAttributes()) : "");
        } else {
            return "Element:\n" +
                    "path = " + path + "\n";
        }
    }

    public String printAttributes() {
        if (attributes == null || attributes.isEmpty())
            return "";
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }
}
