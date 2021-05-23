import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserialize {
    public static SendPack deserialize(byte[] obj) throws IOException, ClassNotFoundException {
        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(obj));
        SendPack sp = (SendPack) iStream.readObject();
        iStream.close();
        return sp;
    }

    public static Authorization deserializeAuth(byte[] obj)
    {
        try
        {
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(obj));
            Authorization au = (Authorization) iStream.readObject();
            iStream.close();
            return au;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
