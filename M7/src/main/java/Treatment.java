import java.net.*;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Treatment {
    private static String[] helpText = {
            "info : вывести в стандартный поток вывода информацию о коллекции.",
            "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении.",
            "add {element} : добавить новый элемент в коллекцию.",
            "update id {element} : обновить значение элемента коллекции, id которого равен заданному.",
            "remove_by_id id : удалить элемент из коллекции по его id",
            "clear : очистить коллекцию.",
            "save : сохранить коллекцию в файл.",
            "execute_script file_name : считать и исполнить скрипт из указанного файла.",
            "exit : завершить программу (без сохранения в файл).",
            "head : вывести первый элемент коллекции.",
            "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.",
            "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный.",
            "sum_of_health : вывести сумму значений поля health для всех элементов коллекции.",
            "count_by_health health : вывести количество элементов, значение поля health которых равно заданному.",
            "filter_by_health health : вывести элементы, значение поля health которых равно заданному."
    };

    private static String[] exc = {
            "Колекция пуста.",
            "Элементов не найдено.",
            "Элемент не добавлен.",
            "Не удалено ни одного элемента.",
            "Недостаточно прав",
            "Ошибка авторизации"
    };

    private static String[] info = {
            "Успешно."
    };
    private static ForkJoinPool fjp = new ForkJoinPool();

    private static ReturnPack help () {
        ReturnPack rp = new ReturnPack(helpText, null, null);
        return rp;
    }

    private static ReturnPack head (PriorityQueue<SpaceMarine> pQ) {
        SpaceMarine[] sp = {pQ.peek()};
        ReturnPack rp;
        if (sp!=null) {
            rp = new ReturnPack(null, null, sp);
        } else {
            rp = new ReturnPack(null, exc[0], null);
        }
        return rp;
    }

    private static ReturnPack show (PriorityQueue<SpaceMarine> pQ) {
        ReturnPack rp;
        if (!pQ.isEmpty()) {
            rp = new ReturnPack(null, null, pQ.stream().sorted(new NameComparator()).toArray(SpaceMarine[]::new));
        } else {
            rp = new ReturnPack(null, exc[0], null);
        }
        return rp;
    }

    private static ReturnPack clear (PriorityQueue<SpaceMarine> pQ, String login) {
        if (pQ.size() > 0) {
            SpaceMarine[] sm = pQ.stream().toArray(SpaceMarine[]::new);
            for (SpaceMarine i : sm) {
                if (DBTreatment.remove(i.getId(), login)) {
                    pQ.remove(i);
                }
            }
        }
        ReturnPack rp = new ReturnPack(info, null, null);
        return rp;
    }

    private static ReturnPack sumOfHealth (PriorityQueue<SpaceMarine> pQ) {
        Iterator value = pQ.iterator();
        int hp = 0;
        while (value.hasNext()) {
            hp += ((SpaceMarine) value.next()).getHealth();
        }
        ReturnPack rp = new ReturnPack(new String[]{Integer.toString(hp)}, null, null);
        return rp;
    }

    private static ReturnPack info (PriorityQueue<SpaceMarine> pQ, LocalDateTime tC) {
        ReturnPack rp = new ReturnPack(new String[]{
                "Тип: " + pQ.getClass().getName(),
                "Время инициализации: " + tC,
                "Количество элементов: " + pQ.size()
        }, null, null);
        return rp;
    }

    private static ReturnPack countByHealth (PriorityQueue<SpaceMarine> pQ, String arg) {
        ReturnPack rp = new ReturnPack(new String[]{Integer.toString((int) pQ.stream().filter(x -> x.getHealth() == Integer.parseInt(arg)).count())}, null, null);
        return rp;
    }

    private static ReturnPack filterByHealth (PriorityQueue<SpaceMarine> pQ, String arg) {
        ReturnPack rp;
        SpaceMarine[] sp = pQ.stream().filter(x -> x.getHealth() == Integer.parseInt(arg)).sorted(new NameComparator()).toArray(SpaceMarine[]::new);
        if (sp.length != 0) {
            rp = new ReturnPack(null, null, sp);
        } else {
            rp = new ReturnPack(null, exc[1], null);
        }
        return rp;
    }

    private static ReturnPack removeById (PriorityQueue<SpaceMarine> pQ, String arg, String login) {
        ReturnPack rp;
        Optional<SpaceMarine> sp = pQ.stream().filter(x -> x.getId() == Integer.parseInt(arg)).findFirst();
        if (sp.isPresent()) {
            if (DBTreatment.remove(Integer.parseInt(arg), login)) {
                pQ.remove(sp.get());
                rp = new ReturnPack(info, null, null);
            } else {
                rp = new ReturnPack(null, exc[4], null);
            }
        } else {
            rp = new ReturnPack(null, exc[1], null);
        }
        return rp;
    }

    private static ReturnPack add (PriorityQueue<SpaceMarine> pQ, SpaceMarine sp, String login) {
        int id;
        ReturnPack rp;
        id = DBTreatment.add(sp, login);
        if (id!=0) {
            sp.setId(id);
            pQ.add(sp);
            rp = new ReturnPack(info, null, null);
        } else {
            rp = new ReturnPack(null, exc[2], null);
        }
        return rp;
    }

    private static ReturnPack addIfMin (PriorityQueue<SpaceMarine> pQ, SpaceMarine sp, String login) {
        ReturnPack rp;
        if (pQ.size() != 0) {
            if (sp.hashCode() < pQ.peek().hashCode()) {
                rp = add(pQ, sp, login);
            } else {
                rp = new ReturnPack(null, exc[2], null);
            }
        } else {
            rp = add(pQ, sp, login);
        }
        return rp;
    }

    private static ReturnPack removeGreater (PriorityQueue<SpaceMarine> pQ, SpaceMarine sp, String login) {
        boolean ch = false;
        ReturnPack rp;
        if (pQ.size() > 0) {
            SpaceMarine[] sm = pQ.stream().filter(x -> x.hashCode() > sp.hashCode()).toArray(SpaceMarine[]::new);
            for (SpaceMarine i : sm) {
                if (DBTreatment.remove(i.getId(), login)) {
                    pQ.remove(i);
                    ch = true;
                }
            }
        }
        if (ch) {
            rp = new ReturnPack(info, null, null);
        } else {
            rp = new ReturnPack(null, exc[3], null);
        }
        return rp;
    }

    private static ReturnPack update (PriorityQueue<SpaceMarine> pQ, SpaceMarine sp, String login) {
        ReturnPack rp;
        boolean upd = false;
        Optional<SpaceMarine> sm = pQ.stream().filter(x -> x.getId().equals(sp.getId())).findFirst();
        if (sm.isPresent()) {
            upd = DBTreatment.update(sp, login);
            if (upd) {
                pQ.remove(sm.get());
                pQ.add(sp);
                rp = new ReturnPack(info, null, null);
            } else {
                rp = new ReturnPack(null, exc[4], null);
            }
        } else {
            rp = new ReturnPack(null, exc[1], null);
        }
        return rp;
    }

    public static void treatment (SendPack sp,
                                  LocalDateTime timeCreate,
                                  PriorityQueue<SpaceMarine> pQueue,
                                  DatagramSocket serverSocket,
                                  SocketAddress senderAddress) throws SocketException {
        ReturnPack rp;
        Lock lock = new ReentrantLock();
        lock.lock();
        if (DBTreatment.check(sp.getLogin(), sp.getPassword())) {
            switch (sp.getComand()) {
                case help:
                    rp = Treatment.help();
                    break;
                case head:
                    rp = Treatment.head(pQueue);
                    break;
                case show:
                    rp = Treatment.show(pQueue);
                    break;
                case clear:
                    rp = Treatment.clear(pQueue, sp.getLogin());
                    break;
                case sum_of_health:
                    rp = Treatment.sumOfHealth(pQueue);
                    break;
                case info:
                    rp = Treatment.info(pQueue, timeCreate);
                    break;
                case count_by_health:
                    rp = Treatment.countByHealth(pQueue, sp.getArg());
                    break;
                case filter_by_health:
                    rp = Treatment.filterByHealth(pQueue, sp.getArg());
                    break;
                case remove_by_id:
                    rp = Treatment.removeById(pQueue, sp.getArg(), sp.getLogin());
                    break;
                case add:
                    rp = Treatment.add(pQueue, sp.getSp(), sp.getLogin());
                    break;
                case add_if_min:
                    rp = Treatment.addIfMin(pQueue, sp.getSp(), sp.getLogin());
                    break;
                case remove_greater:
                    rp = Treatment.removeGreater(pQueue, sp.getSp(), sp.getLogin());
                    break;
                case update:
                    rp = Treatment.update(pQueue, sp.getSp(), sp.getLogin());
                    break;
                default:
                    rp = null;
                    break;
            }
        } else {
            rp = new ReturnPack(null, exc[5], null);
        }
        lock.unlock();

        if (rp!=null) {
//            Sending.sanding(rp, serverSocket, senderAddress);
            Sending send = new Sending(rp, serverSocket, senderAddress);
            fjp.invoke(send);
        }
    }

    public static void authorization (Authorization au, DatagramSocket serverSocket, SocketAddress senderAddress) throws SocketException {
        StatusAuth sa;
        Lock lock = new ReentrantLock();
        lock.lock();
        sa = DBTreatment.authuser(au);
        lock.unlock();
        if (sa!=null) {
//            Sending.sanding(sa, serverSocket, senderAddress);
            Sending send = new Sending(sa, serverSocket, senderAddress);
            fjp.invoke(send);
        }
    }
}
