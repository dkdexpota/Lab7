import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class Deserialize {
    public static ReturnPack deserialize(byte[] obj)
    {
        try
        {
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(obj));
            ReturnPack rp = (ReturnPack) iStream.readObject();
            iStream.close();
            return rp;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static StatusAuth deserializest(byte[] obj)
    {
        try
        {
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(obj));
            StatusAuth st = (StatusAuth) iStream.readObject();
            iStream.close();
            return st;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}