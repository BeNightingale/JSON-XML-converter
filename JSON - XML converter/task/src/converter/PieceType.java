package converter;

public enum PieceType {
    OPENING_TAG, ENDING_TAG, NULL_VALUE_TAG, VALUE

    // TAG  this name includes first three: OPENING_TAG, ENDING_TAG, NULL_VALUE_TAG. TAG: <something inside>
    // OPENING_TAG: <employee> or <employee id="112"> or <employee id="112" type="cook">
    // ENDING_TAG: </employee>
    // NULL_VALUE_TAG: <employee /> or <attr id="1" /> or <date day="12" month="12" year="2018"/>
    // VALUE: 6753322 or to_example@gmail.com or empty???
}
