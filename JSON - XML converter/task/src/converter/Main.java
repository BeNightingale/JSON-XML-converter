package converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        // This program allows to convert individual xml or json elements
        String output = "";
        //    Path path = Path.of("JSON - XML converter/task/src/converter/test.txt"); // Helps to test examples
        Path path = Path.of("test.txt");   // file for testing solution by system
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            if (line.startsWith("<")) {
                output = convertXmlToJson(line);
            } else if (line.startsWith("{")) {
                final StringBuilder stringBuilder = new StringBuilder();
                lines.forEach(stringBuilder::append);
                output = convertJsonToXml(stringBuilder.toString());
            }
            // System.out.println(output);
        }
        System.out.println(output);
    }

    private static String convertXmlToJson(String stringXmlLine) {
        String[] array = stringXmlLine.split(">");
        int len = array.length;
        String key = array[0].substring(1).replace("/", "");
        String[] arrayKey = key.split(" ");
        int keyLength = arrayKey.length;
        Map<String, String> attributeMap = Collections.emptyMap();
        if (keyLength > 1) {
            attributeMap = new HashMap<>();
            for (int i = 1; i < keyLength - 2; i += 3) {
                attributeMap.put(arrayKey[i], arrayKey[i + 2]);
            }
        }
        String value = (len == 1) ? null : array[1].split("<")[0];
        return createJson(keyLength == 1 ? key : arrayKey[0], value, attributeMap);
    }

    private static String createJson(String key, String value, Map<String, String> attributeMap) {
        String formattedValue = value == null ? "null" : String.format("\"%s\"", value);
        if (attributeMap.isEmpty()) {
            return String.format("{%n\"%s\":%s%n}", key, formattedValue);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
                stringBuilder.append("\"@").append(entry.getKey()).append("\":").append(entry.getValue()).append(",\n\t\t");
            }
            stringBuilder.append("\"#").append(key).append("\":");
            return String.format("{%n\t\"%s\":{%n\t\t%s%s%n\t}%n}", key, stringBuilder, formattedValue);
        }
    }

    private static String convertJsonToXml(String stringJsonLine) {
        String[] array = stringJsonLine.replace("{", "")
                .replace("}", "")
                .replace("\t", "")
                .replace(" ", "")
                .replace("\n", "")
                .replace(",", ":")
                .split(":");
        String key = array[0].replace("\"", "");
        int arrayLength = array.length;
        Map<String, String> attributesMap = Collections.emptyMap();
        if (arrayLength > 2) {
            attributesMap = new HashMap<>();
            for (int i = 1; i < arrayLength - 3; i += 2) {
                attributesMap.put(
                        array[i].replace("@", "").replace("\"", ""),
                        array[i + 1].startsWith("\"") ? array[i + 1] : ("\"" + array[i + 1] + "\"")
                );
            }
        }
        String value = array[arrayLength - 1].contains("\"") ? array[arrayLength - 1].replace("\"", "") : array[arrayLength - 1];
        return createXml(key, value, attributesMap);
    }

    private static String createXml(String key, String value, Map<String, String> attributesMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!attributesMap.isEmpty()) {
            for (Map.Entry<String, String> entry : attributesMap.entrySet()) {
                stringBuilder.append(" ").append(entry.getKey()).append(" = ").append(entry.getValue());
            }
        }
        String xmlNoNull = String.format("<%s%s>%s</%s>", key, stringBuilder, value.replace("\"", ""), key);
        String xmlWithNull = String.format("<%s%s/>", key, stringBuilder);
        return "null".equals(value) ? xmlWithNull : xmlNoNull;
    }
}
