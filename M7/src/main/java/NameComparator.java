import java.util.Comparator;
/**
 * Класс компаратор для PriorityQueue с объектами SpaceMarine, сортировка производится по возрастанию значения Name.
 * @author я
 */
class NameComparator implements Comparator<SpaceMarine> {
    public int compare(SpaceMarine s1, SpaceMarine s2) {
        if (s1.getName().length() > s2.getName().length())
            return 1;
        else if (s1.getName().length() < s2.getName().length())
            return -1;
        return 0;
    }
}