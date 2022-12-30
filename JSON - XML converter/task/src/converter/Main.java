package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String output = "";
        if (input.startsWith("<")) {
            output = convertXmlToJson(input);
        } else if (input.startsWith("{")) {
            output = convertJsonToXml(input);
        }
        System.out.println(output);
    }

    private static String convertXmlToJson(String stringXmlLine) {
        String[] array = stringXmlLine.split(">");
        int len = array.length;
        String key = array[0].substring(1).replace("/", "");
        String value = len == 1 ? null : array[1].split("<")[0];

        return createJson(key, value);
    }

    private static String createJson(String key, String value) {
        String formattedValue = value == null ? "null" : String.format("\"%s\"", value);
        return String.format("{\"%s\":%s}", key, formattedValue);
    }

    private static String convertJsonToXml(String stringJsonLine) {
        String[] array = stringJsonLine.replace("{", "").replace("}", "").split(":");
        String key = array[0].replace("\"", "");
        String value = array[1].contains("\"") ? array[1].replace("\"", "") : array[1];
        return createXml(key, value);
    }

    private static String createXml(String key, String value) {
        String xmlNoNull = String.format("<%s>%s</%s>", key, value.replace("\"", ""), key);
        String xmlWithNull = String.format("<%s/>", key);
        return "null".equals(value) ? xmlWithNull : xmlNoNull;
    }
}
