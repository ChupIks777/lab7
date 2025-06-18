package collectionobject;
import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private Long x; //Максимальное значение поля: 810, Поле не может быть null
    private Float y; //Максимальное значение поля: 289, Поле не может быть null

    public Coordinates(Long x, Float y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public boolean validate() {
        if (x == null || x > 810) return false;
        return y != null && x <= 289;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates location = (Coordinates) object;
        return Float.compare(y, location.y) == 0 && Objects.equals(x, location.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Координаты: (" +
                "x=" + x +
                ", y=" + y +
                ')';
    }
}
