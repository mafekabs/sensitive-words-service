package za.co.bts.words.sensitive.dto;

public enum SenderIdType {
    SWSERVICE_V1("SWSERVICE_V1"),
    SWSERVICE_V2("SWSERVICE_V2");

    private String value;
    SenderIdType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SenderIdType getSenderIdTypeByValue(String value) {
        for (SenderIdType senderIdType : SenderIdType.values()) {
            if (senderIdType.getValue().equals(value)) {
                return senderIdType;
            }
        }
        return null;
    }
}