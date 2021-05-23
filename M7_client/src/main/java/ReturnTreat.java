public class ReturnTreat {
    private static void objpols(SpaceMarine o) {
        System.out.println("id: " + o.getId().toString());
        System.out.println("name: " + o.getName());
        System.out.println("coordinates: X: " + o.getCoordinates().getX() + " Y: " + o.getCoordinates().getY());
        System.out.println("creationDate: " + o.getCreationDate().toString());
        System.out.println("health: " + o.getHealth());
        System.out.println("achievements: " + o.getAchievements());
        System.out.println("category: " + o.getCategory());
        System.out.println("weaponType: " + o.getWeaponType());
        if (o.getChapter() != null) {
            System.out.println("chapter: name: " + o.getChapter().getName() + " marinesCount: " + o.getChapter().getMarinesCount());
        } else {
            System.out.println("chapter: " + o.getChapter());
        }
    }

    static public void treatment (ReturnPack rp) {
        if (rp.getExc()!=null) {
            System.out.println(rp.getExc());
        } else {
            if (rp.getInfo()!=null) {
                for (int i = 0; i<rp.getInfo().length; i++) {
                    System.out.println(rp.getInfo()[i]);
                }
            }
            if (rp.getSp()!=null) {
                for (int i = 0; i<rp.getSp().length; i++) {
                    if (rp.getSp()[i]!=null) {
                        objpols(rp.getSp()[i]);
                    }
                }
            }
        }
    }
}
