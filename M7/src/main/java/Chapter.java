import java.io.Serializable;

/**
 * Класс Chapter с какими-то двумя полями String и Integer.
 * @author я
 */
public class Chapter implements Serializable {
    private static final long serialVersionUID = 4L;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Integer marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000

    public String getName() {
        return name;
    }

    public Integer getMarinesCount() {
        return marinesCount;
    }

    public Chapter(String name, Integer marinesCount) {
        this.name = name;
        this.marinesCount = marinesCount;
    }
}