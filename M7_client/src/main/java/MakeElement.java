import java.time.ZonedDateTime;
import java.util.Scanner;

public class MakeElement {
    static String[] exc = new String[] {
            "Имя не может быть пустым.",
            "Введите целое число.",
            "Введите число.",
            "Число должно быть положительным.",
            "Введите целое число.",
            "Введите значение из списка.",
            "Число должно быть от 1 до 1000."
//            "The name cannot be empty",
//            "Enter an integer",
//            "Enter a number",
//            "The number must be positive",
//            "Enter an integer",
//            "Enter a value from the list",
//            "The number must be between 1 and 1000"
    };
    static String[] info = new String[] {
            "name: ",
            "X: ",
            "Y: ",
            "health: ",
            "achievements: ",
            "category (ASSAULT, INCEPTOR, HELIX, APOTHECARY): ",
            "weaponType (MELTAGUN, COMBI_PLASMA_GUN, GRAV_GUN, null - пустая строка): ",
            "chapter name: ",
            "chapter marinesCount: "
    };
    public static SpaceMarine makeElement(int id){
        ZonedDateTime creationDate = ZonedDateTime.now();
        Scanner in = new Scanner(System.in);

        String name;
        while (true) {
            System.out.print(info[0]);
            name = in.nextLine();
            if (name.length() == 0){
                System.out.println(exc[0]);
            }
            else {
                break;
            }
        }

        int x;
        while (true) {
            System.out.print(info[1]);
            try {
                x = Integer.parseInt(in.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println(exc[1]);
            }
        }

        double y;
        while (true) {
            System.out.print(info[2]);
            try {
                y = Double.parseDouble(in.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println(exc[2]);
            }
        }

        long health;
        while (true) {
            System.out.print(info[3]);
            try {
                health = Long.parseLong(in.nextLine());
                if (health>0) {
                    break;
                } else {
                    System.out.println(exc[3]);
                }
            } catch (NumberFormatException e) {
                System.out.println(exc[4]);
            }
        }

        System.out.print(info[4]);
        String achievements = in.nextLine();
        if (achievements.length()==0) {
            achievements = null;
        }

        AstartesCategory category;
        while (true) {
            System.out.print(info[5]);
            try {
                category = AstartesCategory.valueOf(in.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(exc[5]);
            }
        }

        Weapon weaponType;
        String chek_null;
        while (true) {
            System.out.print(info[6]);
            chek_null = in.nextLine();
            if (chek_null.length()==0) {
                weaponType = null;
                break;
            } else {
                try {
                    weaponType = Weapon.valueOf(chek_null);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(exc[5]);
                }
            }
        }

        System.out.print(info[7]);
        String chname = in.nextLine();
        Chapter chapter = null;
        if (chname.length() > 0) {
            Integer marinesCount;
            while (true) {
                System.out.print(info[8]);
                chek_null = in.nextLine();
                if (chek_null.length() == 0) {
                    marinesCount = null;
                    chapter = new Chapter(chname, marinesCount);
                    break;
                } else {
                    try {
                        marinesCount = Integer.parseInt(chek_null);
                        if (marinesCount <= 1000 && marinesCount > 0) {
                            chapter = new Chapter(chname, marinesCount);
                            break;
                        } else {
                            System.out.println(exc[6]);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(exc[4]);
                    }
                }
            }
        }
        return new SpaceMarine(id, name, new Coordinates(x, y), creationDate, health, achievements, category, weaponType, chapter);
    }
}
