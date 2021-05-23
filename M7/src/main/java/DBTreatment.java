import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

public class DBTreatment {
    public static SortedSet<Integer> all_id = new TreeSet<>();
    public static PriorityQueue<SpaceMarine> createdb() {
        Connection c;
        Statement stmt;
        PriorityQueue pQueue = new PriorityQueue<SpaceMarine>(new SpaceMarineComparator());
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "***");
            c.setAutoCommit(false);
            System.out.println("-- Opened database successfully");
            String sp;
            String coord;
            String chapter;
            String authorization;
            String order_sp;
            String order_coord;
            String order_chapter;
            String order_authorization;

            //-------------- CREATE TABLE ---------------
            stmt = c.createStatement();
            chapter = "CREATE TABLE IF NOT EXISTS chapter" +
                    "(id            INT PRIMARY KEY NOT NULL," +
                    "name           VARCHAR(100) NOT NULL," +
                    "marinesCount   INT)";
            stmt.executeUpdate(chapter);
            order_chapter = "create sequence if not exists order_chapter";
            stmt.executeUpdate(order_chapter);
            coord = "CREATE TABLE IF NOT EXISTS coordinates " +
                    "(id    INT PRIMARY KEY NOT NULL," +
                    " X     INT NOT NULL," +
                    " Y     REAL NOT NULL)";
            stmt.executeUpdate(coord);
            order_coord = "create sequence if not exists order_coord";
            stmt.executeUpdate(order_coord);
            sp = "CREATE TABLE IF NOT EXISTS spacemarine " +
                    "(id             INT PRIMARY KEY NOT NULL," +
                    " name           VARCHAR(100) NOT NULL, " +
                    " coordinates    INT references coordinates(id) ON DELETE CASCADE NOT NULL, " +
                    " creationdate   VARCHAR(100) NOT NULL, " +
                    " health         INT NOT NULL," +
                    " achievements   VARCHAR(100)," +
                    " category       VARCHAR(11) NOT NULL," +
                    " weaponType     VARCHAR(17), " +
                    " chapter        INT references chapter(id) ON DELETE CASCADE," +
                    " login          VARCHAR(100) NOT NULL)";
            stmt.executeUpdate(sp);
            order_sp = "create sequence if not exists order_sp";
            stmt.executeUpdate(order_sp);
            authorization = "CREATE TABLE IF NOT EXISTS users" +
                    "(id INT PRIMARY KEY NOT NULL," +
                    "login TEXT NOT NULL," +
                    "password VARCHAR(35) NOT NULL)";
            stmt.executeUpdate(authorization);
            order_authorization = "create sequence if not exists order_authorization";
            stmt.executeUpdate(order_authorization);
            stmt.close();
            c.commit();
            System.out.println("-- Table created successfully");

            //--------------- SELECT DATA ------------------
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM spacemarine;" );
            PreparedStatement  psCoord = c.prepareStatement("SELECT * FROM coordinates WHERE id = ?");
            PreparedStatement  psCapter = c.prepareStatement("SELECT * FROM chapter WHERE id = ?");

            while ( rs.next() ) {
                psCoord.setInt(1, rs.getInt("coordinates"));
                ResultSet coordPar = psCoord.executeQuery();
                coordPar.next();
                Chapter chapman = null;
                Weapon weapon = null;
                if (null != rs.getString("chapter")) {
                    psCapter.setInt(1, rs.getInt("chapter"));
                    ResultSet chapterPar = psCapter.executeQuery();
                    chapterPar.next();
                    chapman = new Chapter(chapterPar.getString("name"), Integer.getInteger(chapterPar.getString("marinesCount")));

                }
                if (rs.getString("weaponType")!=null) {
                    weapon = Weapon.valueOf(rs.getString("weaponType"));
                }
                pQueue.add(new SpaceMarine(rs.getInt("id"),
                            rs.getString("name"),
                            new Coordinates(coordPar.getInt("x"), coordPar.getDouble("y")),
                            ZonedDateTime.parse(rs.getString("creationdate")),
                            rs.getLong("health"),
                            rs.getString("achievements"),
                            AstartesCategory.valueOf(rs.getString("category")),
                            weapon,
                            chapman));
                all_id.add(rs.getInt("id"));
            }
            rs.close();
            stmt.close();
            c.commit();
            System.out.println("-- Operation SELECT done successfully");
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("-- All Operations done successfully");
        return pQueue;
    }

    public static StatusAuth authuser (Authorization authorization) {
        Connection c;
        StatusAuth sa = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "***");
            c.setAutoCommit(false);
            PreparedStatement psHave = c.prepareStatement("SELECT * FROM users WHERE login = ?");
            psHave.setString(1, authorization.getLogin());
            ResultSet have = psHave.executeQuery();
            if (have.next()) {
                if (authorization.getCommand().equals("sign in")) {
                    if (have.getString("password").equals(DigestUtils.md5Hex(authorization.getPassword()))) {
                        sa = new StatusAuth("Успешная авторизация", true);
                    } else {
                        sa = new StatusAuth("Неверный логин или пароль.", false);
                    }
                } else {
                    sa = new StatusAuth("Пользователь с таким именем уже существует.", false);
                }
            } else {
                if (authorization.getCommand().equals("sign up")) {
                    PreparedStatement psAuth = c.prepareStatement("insert into users (id, login, password) values (nextval('order_authorization'), ?, ?)");
                    psAuth.setString(1, authorization.getLogin());
                    psAuth.setString(2, DigestUtils.md5Hex(authorization.getPassword()));
                    psAuth.executeUpdate();
                    psAuth.close();
                    c.commit();
                    sa = new StatusAuth("Успешная регистрация, для продолжения авторезируйтесь", false);
                } else {
                    sa = new StatusAuth("Пользователя с таким именем не существует", false);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return sa;
    }

    public static boolean check (String login, String password) {
        Connection c;
        boolean status = false;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "***");
            c.setAutoCommit(false);
            PreparedStatement psHave = c.prepareStatement("SELECT * FROM users WHERE login = ?");
            psHave.setString(1, login);
            ResultSet have = psHave.executeQuery();
            if (have.next() && have.getString("password").equals(DigestUtils.md5Hex(password))) {
                status = true;
            }
            psHave.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return status;
    }

    public static int add (SpaceMarine spaceMarine, String login) {
        Connection c;
        Integer id_coord = null;
        Integer id_chapter = null;
        ResultSet rs;
        int id_sp = 0;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "***");
            // "jdbc:postgresql://localhost:5432/postgres", "postgres", "syh171"
            // "jdbc:postgresql://pg:5432/studs","s311773", "syh171"
            c.setAutoCommit(false);
            // coords
            PreparedStatement psCoord = c.prepareStatement("insert into coordinates (id, x, y) values (nextval('order_coord'), ?, ?)", Statement.RETURN_GENERATED_KEYS);
            psCoord.setInt(1, spaceMarine.getCoordinates().getX());
            psCoord.setDouble(2, spaceMarine.getCoordinates().getY());
            psCoord.executeUpdate();
            rs = psCoord.getGeneratedKeys();
            if (rs.next()) {
                id_coord = rs.getInt(1);
            }
            psCoord.close();
            // chapter
            if (spaceMarine.getChapter() != null) {
                PreparedStatement psChapter = c.prepareStatement("insert into chapter (id, name, marinesCount) values (nextval('order_chapter'), ?, ?)", Statement.RETURN_GENERATED_KEYS);
                psChapter.setString(1, spaceMarine.getChapter().getName());
                if (spaceMarine.getChapter().getMarinesCount()!=null) {
                    psChapter.setInt(2, spaceMarine.getChapter().getMarinesCount());
                } else {
                    psChapter.setNull(2, Types.INTEGER);
                }
                psChapter.executeUpdate();
                rs = psChapter.getGeneratedKeys();
                if (rs.next()) {
                    id_chapter = rs.getInt(1);
                }
                psChapter.close();
            }
            // spacemarine
            PreparedStatement psSpacemarine =
                    c.prepareStatement("insert into spacemarine (id, name, coordinates, creationdate, health, achievements, category, weaponType, chapter, login) " +
                            "values (nextval('order_sp'), ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            psSpacemarine.setString(1, spaceMarine.getName());
            psSpacemarine.setInt(2, id_coord);
            psSpacemarine.setString(3, spaceMarine.getCreationDate().toString());
            psSpacemarine.setLong(4, spaceMarine.getHealth());
            if (spaceMarine.getAchievements() != null) {
                psSpacemarine.setString(5, spaceMarine.getAchievements()); //null
            } else {
                psSpacemarine.setNull(5, Types.VARCHAR);
            }
            psSpacemarine.setString(6, spaceMarine.getCategory().toString());
            if (spaceMarine.getWeaponType() != null) {
                psSpacemarine.setString(7, spaceMarine.getWeaponType().toString()); //null
            } else {
                psSpacemarine.setNull(7, Types.VARCHAR);
            }
            if (id_chapter!=null) {
                psSpacemarine.setInt(8, id_chapter);
            } else {
                psSpacemarine.setNull(8, Types.INTEGER);
            }
            psSpacemarine.setString(9, login);
            psSpacemarine.executeUpdate();
            rs = psSpacemarine.getGeneratedKeys();
            if (rs.next()) {
                id_sp = rs.getInt(1);
            }
            psSpacemarine.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id_sp;
    }

    public static boolean update (SpaceMarine spaceMarine, String login) {
        Connection c;
        ResultSet rs;
        Integer id_chapter = null;
        boolean upd = false;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "***");
            c.setAutoCommit(false);
            PreparedStatement psElem = c.prepareStatement("SELECT * FROM spacemarine WHERE id = ?");
            psElem.setInt(1, spaceMarine.getId());
            rs = psElem.executeQuery();
            if (rs.next()) {
                if (rs.getString("login").equals(login)) {
                    // coord
                    PreparedStatement psCoord = c.prepareStatement("update coordinates set x = ?, y = ? where id = ?");
                    psCoord.setInt(1, spaceMarine.getCoordinates().getX());
                    psCoord.setDouble(2, spaceMarine.getCoordinates().getY());
                    psCoord.setInt(3, rs.getInt("coordinates"));
                    psCoord.executeUpdate();
                    psCoord.close();
                    // chapter
                    if (rs.getString("chapter") != null) {
                        if (spaceMarine.getChapter() != null) {
                            id_chapter = rs.getInt("chapter");
                            PreparedStatement psChapter = c.prepareStatement("update coordinates set name = ?, marinesCount = ? where id = ?");
                            psChapter.setString(1, spaceMarine.getChapter().getName());
                            if (spaceMarine.getChapter().getMarinesCount() != null) {
                                psChapter.setInt(2, spaceMarine.getChapter().getMarinesCount());
                            } else {
                                psChapter.setNull(2, Types.INTEGER);
                            }
                            psChapter.setInt(3, rs.getInt("chapter"));
                            psChapter.executeUpdate();
                            psChapter.close();
                        } else {
                            PreparedStatement psChapter = c.prepareStatement("delete from coordinates where id = ?");
                            psChapter.setInt(1, rs.getInt("chapter"));
                            psChapter.executeUpdate();
                            psChapter.close();
                        }
                    } else if (spaceMarine.getChapter() != null) {
                        PreparedStatement psChapter = c.prepareStatement("insert into chapter (id, name, marinesCount) values (nextval('order_chapter'), ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        psChapter.setString(1, spaceMarine.getChapter().getName());
                        if (spaceMarine.getChapter().getMarinesCount() != null) {
                            psChapter.setInt(2, spaceMarine.getChapter().getMarinesCount());
                        } else {
                            psChapter.setNull(2, Types.INTEGER);
                        }
                        psChapter.executeUpdate();
                        rs = psChapter.getGeneratedKeys();
                        if (rs.next()) {
                            id_chapter = rs.getInt(1);
                        }
                        psChapter.close();
                    }
                    // spacemarine
                    PreparedStatement psSpacemarine = c.prepareStatement("update spacemarine set name = ?, creationdate = ?, health = ?, achievements = ?, category = ?, weaponType = ?, chapter = ? where id = ?");
                    psSpacemarine.setString(1, spaceMarine.getName());
                    psSpacemarine.setString(2, spaceMarine.getCreationDate().toString());
                    psSpacemarine.setLong(3, spaceMarine.getHealth());
                    if (spaceMarine.getAchievements() != null) {
                        psSpacemarine.setString(4, spaceMarine.getAchievements()); //null
                    } else {
                        psSpacemarine.setNull(4, Types.VARCHAR);
                    }
                    psSpacemarine.setString(5, spaceMarine.getCategory().toString());
                    if (spaceMarine.getWeaponType() != null) {
                        psSpacemarine.setString(6, spaceMarine.getWeaponType().toString()); //null
                    } else {
                        psSpacemarine.setNull(6, Types.VARCHAR);
                    }
                    if (id_chapter != null) {
                        psSpacemarine.setInt(7, id_chapter);
                    } else {
                        psSpacemarine.setNull(7, Types.INTEGER);
                    }
                    psSpacemarine.setInt(8, spaceMarine.getId());
                    psSpacemarine.executeUpdate();
                    psSpacemarine.close();
                    c.commit();
                    upd = true;
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upd;
    }

    public static boolean remove (int id, String login) {
        Connection c;
        ResultSet rs;
        boolean rm = false;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "***");
            c.setAutoCommit(false);
            PreparedStatement psElem = c.prepareStatement("SELECT * FROM spacemarine WHERE id = ?");
            psElem.setInt(1, id);
            rs = psElem.executeQuery();
            if (rs.next()) {
                if (rs.getString("login").equals(login)) {
                    // удаление
                    PreparedStatement psSpacemarine = c.prepareStatement("delete from spacemarine where id = ?");
                    psSpacemarine.setInt(1, id);
                    psSpacemarine.executeUpdate();
                    psSpacemarine.close();
                    c.commit();
                    rm = true;
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rm;
    }
}
