package Domain;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * Representa una configuracion completa de nivel.
 * Contiene el tablero, enemigos, monedas, elementos especiales y posiciones de inicio y fin.
 * Puede cargarse desde archivos .txt o crearse programaticamente.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    private int number;
    private String name;
    private Table table;
    private List<Enemy> enemies;
    private List<Coin> coins;
    private List<SpecialElement> specialElements;
    private int timeLimit;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    /**
     * Crea un nivel con numero y nombre.
     *
     * @param number numero del nivel
     * @param name nombre descriptivo
     */
    public Level(int number, String name) {
        this.number = number;
        this.name = name;
        this.enemies = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.specialElements = new ArrayList<>();
        this.timeLimit = 60;
    }

    /**
     * Carga una configuracion de nivel desde un archivo de texto.
     *
     * Formato del archivo:
     *   Linea 1: rows cols timeLimit
     *   Linea 2: startX startY endX endY
     *   Linea 3: ZONES count
     *     cada una: type x y height broad
     *   Linea 4: COINS count
     *     cada una: type x y
     *   Linea 5: ENEMIES count
     *     cada uno: type x y dx dy
     *   Linea 6: WALLS count
     *     cada una: x y
     *   Linea 7: SPECIALS count
     *     cada uno: type x y
     *
     * @param filename ruta al archivo de configuracion
     * @param levelNumber numero de nivel a asignar
     * @return el nivel cargado
     * @throws DOPOException si el archivo no existe o tiene formato invalido
     */
    public static Level loadFromFile(String filename, int levelNumber) throws DOPOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            Level level = new Level(levelNumber, "Level " + levelNumber);

            String[] dim = br.readLine().trim().split("\\s+");
            int rows = Integer.parseInt(dim[0]);
            int cols = Integer.parseInt(dim[1]);
            level.timeLimit = Integer.parseInt(dim[2]);
            level.table = new Table(rows, cols);

            String[] pos = br.readLine().trim().split("\\s+");
            level.startX = Integer.parseInt(pos[0]);
            level.startY = Integer.parseInt(pos[1]);
            level.endX = Integer.parseInt(pos[2]);
            level.endY = Integer.parseInt(pos[3]);

            String[] zoneLine = br.readLine().trim().split("\\s+");
            int zoneCount = Integer.parseInt(zoneLine[1]);
            for (int i = 0; i < zoneCount; i++) {
                String[] z = br.readLine().trim().split("\\s+");
                Zone.ZoneType zt = Zone.ZoneType.valueOf(z[0]);
                level.table.addZone(new Zone(
                    Integer.parseInt(z[1]), Integer.parseInt(z[2]),
                    Integer.parseInt(z[3]), Integer.parseInt(z[4]), zt));
            }

            String[] coinLine = br.readLine().trim().split("\\s+");
            int coinCount = Integer.parseInt(coinLine[1]);
            for (int i = 0; i < coinCount; i++) {
                String[] c = br.readLine().trim().split("\\s+");
                Coin.CoinType ct = Coin.CoinType.valueOf(c[0]);
                level.coins.add(new Coin(Integer.parseInt(c[1]), Integer.parseInt(c[2]), ct));
            }

            String[] enemyLine = br.readLine().trim().split("\\s+");
            int enemyCount = Integer.parseInt(enemyLine[1]);
            for (int i = 0; i < enemyCount; i++) {
                String[] e = br.readLine().trim().split("\\s+");
                Enemy.EnemyType et = Enemy.EnemyType.valueOf(e[0]);
                level.enemies.add(new Enemy(
                    Double.parseDouble(e[1]), Double.parseDouble(e[2]),
                    Double.parseDouble(e[3]), Double.parseDouble(e[4]), et));
            }

            String[] wallLine = br.readLine().trim().split("\\s+");
            int wallCount = Integer.parseInt(wallLine[1]);
            for (int i = 0; i < wallCount; i++) {
                String[] w = br.readLine().trim().split("\\s+");
                level.table.setWall(Integer.parseInt(w[1]), Integer.parseInt(w[0]), true);
            }

            String[] specLine = br.readLine().trim().split("\\s+");
            int specCount = Integer.parseInt(specLine[1]);
            for (int i = 0; i < specCount; i++) {
                String[] s = br.readLine().trim().split("\\s+");
                SpecialElement.ElementType set = SpecialElement.ElementType.valueOf(s[0]);
                level.specialElements.add(new SpecialElement(
                    Integer.parseInt(s[1]), Integer.parseInt(s[2]), set));
            }

            return level;

        } catch (IOException e) {
            throw new DOPOException(DOPOException.FILE_NOT_FOUND + filename);
        } catch (Exception e) {
            throw new DOPOException(DOPOException.INVALID_CONFIG + e.getMessage());
        }
    }

    /**
     * Crea la configuracion clasica del nivel 1 
     * Tablero 10x15, 5 monedas amarillas, 4 enemigos basicos.
     *
     * @return nivel 1 configurado
     */
    public static Level createClassicLevel1() {
        Level level = new Level(1, "");
        level.table = new Table(10, 15);
        level.timeLimit = 120;
        level.startX = 1;
        level.startY = 5;
        level.endX = 13;
        level.endY = 5;

        level.table.addZone(new Zone(0, 0, 10, 2, Zone.ZoneType.INITIAL));
        level.table.addZone(new Zone(13, 0, 10, 2, Zone.ZoneType.FINAL));

        level.coins.add(new Coin(5, 3, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(7, 5, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(9, 3, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(6, 7, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(8, 6, Coin.CoinType.YELLOW));

        level.enemies.add(new Enemy(4, 2, 1, 0, Enemy.EnemyType.BASIC));
        level.enemies.add(new Enemy(10, 4, 1, 0, Enemy.EnemyType.BASIC));
        level.enemies.add(new Enemy(6, 6, 1, 0, Enemy.EnemyType.BASIC));
        level.enemies.add(new Enemy(8, 8, 1, 0, Enemy.EnemyType.BASIC));

        return level;
    }

    /**
     * Crea el nivel 2 con enemigos verticales y zona intermedia.
     * Tablero 12x18, 7 monedas (incluye skin coin), 6 enemigos mixtos.
     *
     * @return nivel 2 configurado
     */
    public static Level createLevel2() {
        Level level = new Level(2,"");
        level.table = new Table(12, 18);
        level.timeLimit = 90;
        level.startX = 1;
        level.startY = 6;
        level.endX = 16;
        level.endY = 6;

        level.table.addZone(new Zone(0, 0, 12, 2, Zone.ZoneType.INITIAL));
        level.table.addZone(new Zone(8, 4, 4, 2, Zone.ZoneType.INTERMEDIATE));
        level.table.addZone(new Zone(16, 0, 12, 2, Zone.ZoneType.FINAL));

        level.coins.add(new Coin(4, 3, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(6, 6, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(5, 9, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(11, 3, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(13, 7, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(14, 5, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(10, 9, Coin.CoinType.SKIN_BLUE));

        level.enemies.add(new Enemy(4, 1, 0, 1, Enemy.EnemyType.VERTICAL));
        level.enemies.add(new Enemy(7, 1, 0, 1, Enemy.EnemyType.VERTICAL));
        level.enemies.add(new Enemy(5, 5, 1, 0, Enemy.EnemyType.BASIC));
        level.enemies.add(new Enemy(11, 2, 1, 0, Enemy.EnemyType.BASIC));
        level.enemies.add(new Enemy(13, 8, 0, 1, Enemy.EnemyType.VERTICAL));
        level.enemies.add(new Enemy(15, 3, 1, 0, Enemy.EnemyType.FAST));

        return level;
    }

    /**
     * Crea el nivel 3 con bombas, fuentes de vida y paredes.
     * Tablero 10x20, 7 monedas (incluye skin coins), 5 enemigos, 3 elementos especiales.
     *
     * @return nivel 3 configurado
     */
    public static Level createLevel3() {
        Level level = new Level(3, "");
        level.table = new Table(10, 20);
        level.timeLimit = 75;
        level.startX = 1;
        level.startY = 5;
        level.endX = 18;
        level.endY = 5;

        level.table.addZone(new Zone(0, 0, 10, 2, Zone.ZoneType.INITIAL));
        level.table.addZone(new Zone(18, 0, 10, 2, Zone.ZoneType.FINAL));

        level.coins.add(new Coin(5, 2, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(8, 5, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(10, 7, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(13, 3, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(15, 6, Coin.CoinType.YELLOW));
        level.coins.add(new Coin(7, 8, Coin.CoinType.SKIN_GREEN));
        level.coins.add(new Coin(12, 2, Coin.CoinType.SKIN_RED));

        level.enemies.add(new Enemy(4, 3, 1, 0, Enemy.EnemyType.BASIC));
        level.enemies.add(new Enemy(7, 1, 0, 1, Enemy.EnemyType.VERTICAL));
        level.enemies.add(new Enemy(11, 5, 1, 0, Enemy.EnemyType.FAST));
        level.enemies.add(new Enemy(14, 2, 0, 1, Enemy.EnemyType.VERTICAL));
        level.enemies.add(new Enemy(16, 7, 1, 0, Enemy.EnemyType.BASIC));

        level.specialElements.add(new SpecialElement(6, 5, SpecialElement.ElementType.LIFE_SOURCE));
        level.specialElements.add(new SpecialElement(9, 4, SpecialElement.ElementType.BOMB));
        level.specialElements.add(new SpecialElement(14, 8, SpecialElement.ElementType.BOMB));

        level.table.setWall(4, 10, true);
        level.table.setWall(5, 10, true);
        level.table.setWall(6, 10, true);

        return level;
    }

    /** @return numero del nivel */
    public int getNumber() { return number; }

    /** @return nombre del nivel */
    public String getName() { return name; }

    /** @return tablero del nivel */
    public Table getTable() { return table; }

    /** @return lista de enemigos */
    public List<Enemy> getEnemies() { return enemies; }

    /** @return lista de monedas */
    public List<Coin> getCoins() { return coins; }

    /** @return lista de elementos especiales */
    public List<SpecialElement> getSpecialElements() { return specialElements; }

    /** @return tiempo limite en segundos */
    public int getTimeLimit() { return timeLimit; }

    /** @return columna de inicio del jugador */
    public int getStartX() { return startX; }

    /** @return fila de inicio del jugador */
    public int getStartY() { return startY; }

    /** @return columna de la meta */
    public int getEndX() { return endX; }

    /** @return fila de la meta */
    public int getEndY() { return endY; }
}
