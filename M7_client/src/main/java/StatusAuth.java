import java.io.Serializable;

public class StatusAuth implements Serializable {
    private static final long serialVersionUID = 5L;
    private String message;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
