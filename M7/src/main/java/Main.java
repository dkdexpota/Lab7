import java.io.*;
import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final String[] exc = {
            "Файл не найден.",
            "Ошибка структуры файла.",
            "Ошибка ID элементов."
    };
    public static Lock glock = new ReentrantLock();
    public static void main(String[] args) throws IOException, InterruptedException {
        PriorityQueue<SpaceMarine> pQueue = DBTreatment.createdb();
        LocalDateTime time_create = LocalDateTime.now();
        Runnable read = new Runnable() {
            @Override
            public void run() {
                try {
                    Receiving.receiving(pQueue, time_create);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        glock.lock();
        new Thread(read).start();
        boolean gose = true;
        while (gose) {
            System.out.print("exit: ");
            Scanner in = new Scanner(System.in);
            switch (in.nextLine()) {
                case ("exit"):
                    gose = false;
                    glock.unlock();
                    break;
                default:
                    System.out.println("Неизвесная команда.");
                    break;
            }
        }
    }
}
