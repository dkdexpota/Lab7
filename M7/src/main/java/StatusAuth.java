import java.io.Serializable;

public class StatusAuth implements Serializable {
    private static final long serialVersionUID = 5L;
    private String message;
    private boolean status;

    public StatusAuth (String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}
