import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 3L;
    private int x;
    private Double y; //Поле не может быть null

    public int getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public  Coordinates (int x, Double y) {
        this.x = x;
        this.y = y;
    }
}