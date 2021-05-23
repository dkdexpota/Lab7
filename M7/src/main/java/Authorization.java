import java.io.Serializable;

public class Authorization implements Serializable {
    private static final long serialVersionUID = 4L;
    private String command;
    private String login;
    private String password;
    public Authorization (String command, String login, String password) {
        this.command = command;
        this.login = login;
        this.password = password;
    }

    public String getCommand() {
        return command;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
