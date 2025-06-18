package collectionobject;

import java.io.Serializable;

public enum Color implements Serializable {
    RED("красный"),
    BLACK("черный"),
    BLUE("синий"),
    YELLOW("желтый"),
    WHITE("белый");

    private final String type;
    Color(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
