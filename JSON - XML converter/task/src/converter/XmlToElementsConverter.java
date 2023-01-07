package converter;

import java.util.*;

import static converter.PieceType.*;

public class XmlToElementsConverter {

    public List<String> divideIntoPieces(String xmlInput) { //first check it's xml, before using this method
        String[] array = xmlInput.split("");
        List<String> piecesList = new ArrayList<>();
        for (int i = 0; i < array.length;) {
            List<String> pieces = new ArrayList<>();
            if ("<".equals(array[i])) {
                do {
                    pieces.add(array[i]);
                    i++;
                } while (!">".equals(array[i]));
                pieces.add(array[i]);
                i++;
            } else {
                do {
                    pieces.add(array[i]);
                    i++;
                } while (!"<".equals(array[i]));
            }
            String s = concatListToOneString(pieces);
            piecesList.add(concatListToOneString(pieces));
        }

        return piecesList;
    }

    public List<SinglePiece> convertPiecesListIntoSinglePiecesList(List<String> piecesList) {
        List<SinglePiece> singlePieceList = new ArrayList<>();
        for (String piece : piecesList) {
            SinglePiece singlePiece;
            if (isPieceTAG(piece)) {
                String name = findName(piece);
                if (isPieceENDING_TAG(piece)) {
                    singlePiece = new SinglePiece(name, piece, PieceType.ENDING_TAG);
                } else if (isPieceNULL_VALUE_TAG(piece)) {
                    singlePiece = new SinglePiece(name, piece, NULL_VALUE_TAG);
                } else {
                    singlePiece = new SinglePiece(name, piece, OPENING_TAG);
                }
            } else {
                singlePiece = new SinglePiece("", "\"" + piece + "\"", PieceType.VALUE);
            }
            singlePieceList.add(singlePiece);
        }
        return singlePieceList;
    }

    public List<Element> buildElementsTree(List<SinglePiece> singlePieceList) {
        List<Element> elementsList =  new ArrayList<>();
        List<Element> notClosedElementsList = new ArrayList<>();
        SinglePiece root = singlePieceList.get(0);
        Element mainElement = new Element();
        mainElement.setPath(root.getName());
        mainElement.setParent(null);
        mainElement.setAttributes(searchForAttributes(root));
        elementsList.add(mainElement);
        if (root.getPieceType() == NULL_VALUE_TAG) { // dokument z pojedynczym root i nic więcej    KONIEC
            mainElement.setValue(null);
            return  elementsList;
        }
        if (root.getPieceType() == OPENING_TAG) {
            PieceType pieceType = singlePieceList.get(1).getPieceType();
            if (pieceType == VALUE) { // dokument z pojedynczym root z wartością i nic więcej      KONIEC
                mainElement.setValue(singlePieceList.get(1).getPieceContent());
                return  elementsList;
            } else if (pieceType == ENDING_TAG) { // dokument z pojedynczym root zz pustą wartością i nic więcej    KONIEC
                mainElement.setValue("\"\"");
                return  elementsList;
            } else {
                notClosedElementsList.add(mainElement); // mainElement nie jest zamknięty, czyli musi mieć dzieci
                mainElement.setHasChildren(true);
                // teraz sprawdzam, czy root ma dzieci
                //  if (pieceType == OPENING_TAG || pieceType == NULL_VALUE_TAG) {
                Element element = new Element();
//                element.setParent(mainElement);
//                element.setPath(mainElement.getPath() + ", " + singlePieceList.get(1).getName());
//                element.setAttributes(searchForAttributes(singlePieceList.get(1)));
//                if (singlePieceList.get(2).getPieceType() == OPENING_TAG || singlePieceList.get(2).getPieceType() == NULL_VALUE_TAG) {
//                    // później ten nowy się do niego wpisze od dołu po wywołaniu go do tablicy :)
//                    element.setHasChildren(true);
//                }
//                if (singlePieceList.get(2).getPieceType() == ENDING_TAG) {
//                    element.setValue("\"\"");
//                }
//                if (singlePieceList.get(2).getPieceType() == VALUE) {
//                    element.setValue(singlePieceList.get(2).getPieceContent());
//                }
//                elementsList.add(element);
                mainElement.getNestedElementsList().add(element);
            }
                for (int j = 1; j < singlePieceList.size() - 1; j++) { // jak VALUE lub ENDING to nic nie będzie robione tylko pójdzie ++
                    if (singlePieceList.get(j).getPieceType() == NULL_VALUE_TAG || singlePieceList.get(j).getPieceType() == OPENING_TAG) {
                        Element elem = new Element();
                        Element lastOpenedElem = notClosedElementsList.get(notClosedElementsList.size() - 1);
                        elem.setParent(lastOpenedElem);
                        elem.setPath(lastOpenedElem.getPath() + ", " + singlePieceList.get(j).getName());
                        elem.setAttributes(searchForAttributes(singlePieceList.get(j)));
                        if (singlePieceList.get(j).getPieceType() == NULL_VALUE_TAG) {
                            elem.setValue(null);
                        }
                        if (singlePieceList.get(j).getPieceType() == OPENING_TAG) {
                            if (singlePieceList.get(j + 1).getPieceType() == VALUE) {
                                elem.setValue(singlePieceList.get(j + 1).getPieceContent());
                            } else if (singlePieceList.get(j + 1).getPieceType() == ENDING_TAG) {
                                elem.setValue("\"\"");
                            } else {
                                notClosedElementsList.add(elem);
                                elem.setHasChildren(true);
                            }
                        }
                        elementsList.add(elem);
                    }
                }
            }
     //   }
        return elementsList;
    }

    /**
     * Returns map of attributes only for those pieces which can have attributes
     *
     * @param singlePiece one piece of xml document
     * @return attributes map for only tags: OPENING_TAG & NULL_VALUE_TAG
     */
    public Map<String, String> searchForAttributes(SinglePiece singlePiece) {
        if (singlePiece.getPieceType() != NULL_VALUE_TAG && singlePiece.getPieceType() != OPENING_TAG) {
            return Collections.emptyMap();
        }
        Map<String, String> attributesMap = new LinkedHashMap<>();
        String interior = singlePiece.getPieceContent()
                .replace(">", "")
                .replace("<", "")
                .replace("/", "")
                .replace(singlePiece.getName(), "");
        if (interior.length() > 1) {
            String[] arrayWithoutSpaces = interior.split(" ");
            int len = arrayWithoutSpaces.length;
            for (int i = 0; i < len; i++) {
                if (arrayWithoutSpaces[i].contains("=") && arrayWithoutSpaces[i].length() > 1)  {
                    String[] keyValue = arrayWithoutSpaces[i].split("=");
                    attributesMap.put(keyValue[0], keyValue[1]);
                }
                if (
                        !arrayWithoutSpaces[i].contains("=")
                        && !arrayWithoutSpaces[i].contains("\"")
                        && !arrayWithoutSpaces[i].isBlank()
                        && "=".equals(arrayWithoutSpaces[i + 1])
                    ) {
                    attributesMap.put(arrayWithoutSpaces[i], arrayWithoutSpaces[i + 2]);
                }
            }
        }
        return attributesMap;
    }

    private String findName(String pieceTag) {
        return pieceTag.replace("<", "")
                .replace(">", "")
                .replace("/", "")
                .split(" ")[0];
    }

    private String concatListToOneString(List<String> piece) {
        Optional<String> reduce = piece.stream().reduce(String::concat);
        return reduce.orElse("");
    }

    private boolean isPieceTAG(String piece) { // true będzie dla <>, </sss> oraz <sss/>
        return piece.startsWith("<") && piece.endsWith(">");
    }

    private boolean isPieceENDING_TAG(String piece) {
        return piece.startsWith("</");
    }

    private boolean isPieceNULL_VALUE_TAG(String piece) {
        return piece.endsWith("/>");
    }
}
