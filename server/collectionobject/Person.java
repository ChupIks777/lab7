package collectionobject;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Person implements Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime date; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float height; //Поле не может быть null, Значение поля должно быть больше 0
    private long weight; //Значение поля должно быть больше 0
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле не может быть null

    public Person(int id, String name, Coordinates coordinates, ZonedDateTime date, Float height,
                  long weight, Color hairColor, Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime  date) {
        this.date = date;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getHeight() {
        return height;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getWeight() {
        return weight;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Person person = (Person) object;
        return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(coordinates, person.coordinates) &&
                Objects.equals(date, person.date) && Float.compare(height, person.height) == 0 &&
                Objects.equals(weight, person.weight) && hairColor == person.hairColor && nationality == person.nationality &&
                Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, date, height, weight, hairColor, nationality, location);
    }

    @Override
    public String toString() {
        return "Человек{" + "\n" +
                "id: " + id + "\n" +
                "Имя: " + name + "\n" +
                "Координаты: " + coordinates + "\n" +
                "Дата: " + date + "\n" +
                "Рост: " + height + "\n" +
                "Вес: " + weight + "\n" +
                "Цвет волос: " + hairColor + "\n" +
                "Национальность: " + nationality + "\n" +
                "Местоположение: " + location + "\n" +
                '}';
    }
}
