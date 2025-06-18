package collectionobject;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {

    private Integer x; //Поле не может быть null
    private long y;
    private Float z; //Поле не может быть null
    private String name; //Строка не может быть пустой, Поле может быть null

    public Location(Integer x, long y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean validate() {
        if (x == null) return false;
        if (z == null) return false;
        return !Objects.equals(name, "");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        collectionobject.Location location = (collectionobject.Location) object;
        return Objects.equals(y, location.y) && Objects.equals(x, location.x) && Float.compare(z, location.z) == 0 && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, name);
    }

    @Override
    public String toString() {
        return "Местоположение: (" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name=" + name +
                ')';
    }
}
