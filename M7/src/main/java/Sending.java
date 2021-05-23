import java.io.IOException;
import java.net.*;
import java.util.concurrent.RecursiveAction;

public class Sending extends RecursiveAction {
    Object rp;
    DatagramSocket serverSocket;
    SocketAddress address;
//    public static void sanding (Object rp, DatagramSocket serverSocket, SocketAddress address) throws SocketException {
//        byte[] data = Serialize.serialize(rp);
//        try
//        {
//            DatagramPacket dp = new DatagramPacket(data, data.length, address);
//            serverSocket.send(dp);
//        }
//        catch(IOException e)
//        {
//            System.err.println("IOException senderissa " + e);
//        }
//    }
    public Sending (Object rp, DatagramSocket serverSocket, SocketAddress address) {
        this.rp = rp;
        this.serverSocket = serverSocket;
        this.address = address;
    }
    @Override
    protected void compute() {
        byte[] data = Serialize.serialize(rp);
        try
        {
            DatagramPacket dp = new DatagramPacket(data, data.length, address);
            serverSocket.send(dp);
        }
        catch(IOException e)
        {
            System.err.println("IOException senderissa " + e);
        }
    }
}
