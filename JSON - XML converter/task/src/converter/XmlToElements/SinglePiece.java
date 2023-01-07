package converter.XmlToElements;

import converter.XmlToElements.PieceType;

public class SinglePiece {
    String name;
    String pieceContent;
    PieceType pieceType;

    // pieces can be like:
    // <employee> or <employee id="112"> or <employee id="112" type="cook">
    // </employee>
    // <employee /> or <attr id="1" /> or <date day="12" month="12" year="2018"/>
    // 6753322 or to_example@gmail.com or empty???
    public SinglePiece(String name, String pieceContent, PieceType pieceType) {
        this.name = name;
        this.pieceContent = pieceContent;
        this.pieceType = pieceType;
    }

    public SinglePiece() {
    }

    public String getName() {
        return name;
    }

    public String getPieceContent() {
        return pieceContent;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}
