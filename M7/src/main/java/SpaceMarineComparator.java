import java.util.Comparator;
/**
 * Класс компаратор для PriorityQueue с объектами SpaceMarine, сортировка производится по возрастанию значения hashCode.
 * @author я
 */
class SpaceMarineComparator implements Comparator<SpaceMarine> {
    public int compare(SpaceMarine s1, SpaceMarine s2) {
        if (s1.hashCode() > s2.hashCode())
            return 1;
        else if (s1.hashCode() < s2.hashCode())
            return -1;
        return 0;
    }
}