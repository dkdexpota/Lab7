import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Receiving{
    public static void receiving (PriorityQueue<SpaceMarine> pQueue, LocalDateTime time_create) throws IOException, InterruptedException {
        DatagramSocket serverSocket = new DatagramSocket(27914);
        DatagramChannel chan = DatagramChannel.open();
        chan.configureBlocking(false);
        chan.bind(new InetSocketAddress(27913));
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true) {
            if (Main.glock.tryLock()) {
                //                System.out.println("Waiting for a client to connect...");
                //                serverSocket.receive(inputPacket);
                //                InetAddress senderAddress = inputPacket.getAddress();
                //                int senderPort = inputPacket.getPort();
                break;
            } else {
                try {
                    SocketAddress from = chan.receive(buffer);
                    if (from != null) {
                        SocketAddress from1 = new SocketAddress() {
                        };
                        byte[] bytes1 = new byte[0];
                        while (from1 != null) {
                            buffer.flip();
                            int limits = buffer.limit();
                            byte bytes[] = new byte[limits];
                            buffer.get(bytes, 0, limits);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            outputStream.write(bytes);
                            outputStream.write(bytes1);
                            bytes1 = outputStream.toByteArray();
                            buffer.clear();
                            from1 = chan.receive(buffer);
                        }
                        byte[] finalBytes = bytes1;
                        Callable<String> task = () -> {
                            try {
                                Treatment.treatment(Deserialize.deserialize(finalBytes), time_create, pQueue, serverSocket, from);
                            } catch (ClassCastException | ClassNotFoundException | IOException e) {
                                try {
                                    Treatment.authorization(Deserialize.deserializeAuth(finalBytes), serverSocket, from);
                                } catch (SocketException socketException) {
                                    socketException.printStackTrace();
                                }
                            }
                            return null;
                        };
                        executorService.submit(task);
                        buffer.clear();
                    }
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        executorService.shutdownNow();
        serverSocket.close();
    }
}
