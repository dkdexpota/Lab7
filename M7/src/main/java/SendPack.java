import java.io.Serializable;

public class SendPack implements Serializable {
    private static final long serialVersionUID = 1L;
    private Comand comand;
    private SpaceMarine sp;
    private String arg;
    private String login;
    private String password;
    public SendPack (Comand comand, String arg, SpaceMarine sp) {
        this.comand = comand;
        this.arg = arg;
        this.sp = sp;
    }

    public Comand getComand() {
        return comand;
    }

    public String getArg() {
        return arg;
    }

    public SpaceMarine getSp() {
        return sp;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
