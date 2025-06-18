package collectionobject;

import java.io.Serializable;

public enum Country implements Serializable {
    USA("США"),
    GERMANY("Германия"),
    FRANCE("Франция"),
    THAILAND("Тайланд"),
    SOUTH_KOREA("Южная Корея");

    private final String type;
    Country(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
