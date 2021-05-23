import java.io.*;
public class Serialize {
    public static byte[] serialize(Object sp)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(sp);
            oos.close();
            byte[] obj = baos.toByteArray();
            baos.close();
            return obj;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
