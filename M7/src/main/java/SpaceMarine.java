import java.io.Serializable;
import java.time.ZonedDateTime;
/**
 * Основной класс, содержащий поля для обработки json и работы пользователя с данными.
 * @author я
 */
public class SpaceMarine implements Serializable {
    private static final long serialVersionUID = 5L;
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long health; //Значение поля должно быть больше 0
    private String achievements; //Поле может быть null
    private AstartesCategory category; //Поле не может быть null
    private Weapon weaponType; //Поле может быть null
    private Chapter chapter; //Поле может быть null

    public SpaceMarine (int id, String name, Coordinates coordinates, ZonedDateTime creationDate, long health, String achievements,
                        AstartesCategory category, Weapon weaponType, Chapter chapter) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.achievements = achievements;
        this.category = category;
        this.weaponType = weaponType;
        this.chapter = chapter;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() {
        return name;
    }
    public Coordinates getCoordinates() { return coordinates; }
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    public long getHealth() {
        return health;
    }
    public String getAchievements() {
        return achievements;
    }
    public AstartesCategory getCategory() {
        return category;
    }
    public Weapon getWeaponType() {
        return weaponType;
    }
    public Chapter getChapter() {
        return chapter;
    }

}
