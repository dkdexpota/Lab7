import java.io.Serializable;

public class ReturnPack implements Serializable {
    private static final long serialVersionUID = 2L;
    private String[] info;
    private String exc;
    private SpaceMarine[] sp;

    public ReturnPack (String[] info, String exc, SpaceMarine[] sp) {
        this.info = info;
        this.exc = exc;
        this.sp = sp;

    }

    public String[] getInfo() {
        return info;
    }
}
