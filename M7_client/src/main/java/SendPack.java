import java.io.Serializable;

public class SendPack implements Serializable {
    private static final long serialVersionUID = 1L;
    private Comand comand;
    private SpaceMarine sp;
    private String arg;
    private String login;
    private String password;
    public SendPack (Comand comand, String arg, SpaceMarine sp, String login, String password) {
        this.comand = comand;
        this.arg = arg;
        this.sp = sp;
        this.login = login;
        this.password = password;
    }
}