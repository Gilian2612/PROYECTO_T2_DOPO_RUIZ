package Domain;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
/**
 * Controlador principal de logica del juego The DOPO Hardest Game.
 * Gestiona el estado del juego: jugadores, enemigos, monedas, muertes,
 * tiempo y condicion de victoria.
 * La capa de presentacion (whdGUI) llama a esta clase para actualizar y consultar estado.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class worldHardestGame implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Modalidades de juego disponibles. */
    public enum GameMode { PLAYER, PVP, PVM }

    private GameMode mode;
    private Level currentLevel;
    private List<Level> levels;
    private int currentLevelIndex;
    private Player player1;
    private Player player2;
    private boolean paused;
    private boolean gameOver;
    private int timeRemaining;
    private transient Random random;
    /**
     * Crea un nuevo juego con el modo y color dados.
     *
     * @param mode modalidad de juego
     * @param skin1 color del jugador 1
     */
    public worldHardestGame(GameMode mode, Player.Skin skin1) {
        this.mode = mode;
        this.random = new Random();
        this.paused = false;
        this.gameOver = false;
        this.levels = new ArrayList<>();

        // Por ahora solo el nivel 1 esta disponible para la entrega actual.
        levels.add(Level.createClassicLevel1());

        this.currentLevelIndex = 0;
        this.currentLevel = levels.get(0);

        player1 = new Player(currentLevel.getStartX(), currentLevel.getStartY(), skin1);

        this.timeRemaining = currentLevel.getTimeLimit();
    }
    /**
     * Constructor para modo PvP con dos colores.
     *
     * @param skin1 color del jugador 1
     * @param skin2 color del jugador 2
     */
    public worldHardestGame(Player.Skin skin1, Player.Skin skin2) {
        this(GameMode.PVP, skin1);
        player2 = new Player(currentLevel.getEndX(), currentLevel.getEndY(), skin2);
    }

    /**
     * Constructor para modo PvM.
     * La maquina usa movimiento aleatorio.
     *
     * @param skin1 color del jugador 1
     */
    public worldHardestGame(Player.Skin skin1) {
        this(GameMode.PVM, skin1);
        player2 = new Player(currentLevel.getEndX(), currentLevel.getEndY(), Player.Skin.ROJO);
    }

    /**
     * Constructor por defecto para compatibilidad, un jugador, rojo.
     */
    public worldHardestGame() {
        this(GameMode.PLAYER, Player.Skin.ROJO);
    }

    /**
     * Carga una configuracion de nivel desde archivo y la agrega.
     *
     * @param filename ruta al archivo de configuracion
     * @throws DOPOException si el archivo no existe o tiene formato invalido
     */
    public void loadLevelFromFile(String filename) throws DOPOException {
        Level level = Level.loadFromFile(filename, levels.size() + 1);
        levels.add(level);
    }

    /**
     * Inicializa o reinicializa el nivel actual.
     * Reposiciona jugadores y reinicia el tiempo.
     */
    public void initLevel() {
        currentLevel = levels.get(currentLevelIndex);
        player1 = new Player(currentLevel.getStartX(), currentLevel.getStartY(), player1.getOriginalSkin());

        if (mode == GameMode.PVP || mode == GameMode.PVM) {
            Player.Skin s2 = Player.Skin.ROJO;
            if (player2 != null) {
                s2 = player2.getOriginalSkin();
            }
            player2 = new Player(currentLevel.getEndX(), currentLevel.getEndY(), s2);
        }

        timeRemaining = currentLevel.getTimeLimit();
        gameOver = false;
    }

    /**
     * Mueve todos los enemigos un paso.
     * Verifica si alguna bomba destruye a un enemigo.
     */
    public void moveEnemies() {
        if (paused || gameOver) {
            return;
        }

        Table table = currentLevel.getTable();
        List<Enemy> toRemove = new ArrayList<>();

        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.move(table);

            for (SpecialElement se : currentLevel.getSpecialElements()) {
                if (se.affectsEnemy(enemy)) {
                    toRemove.add(enemy);
                }
            }
        }

        currentLevel.getEnemies().removeAll(toRemove);
    }

    /**
     * Mueve al jugador 1 por (dx, dy).
     *
     * @param dx delta horizontal
     * @param dy delta vertical
     */
    public void movePlayer(int dx, int dy) {
        if (paused || gameOver) {
            return;
        }

        player1.move(dx, dy, currentLevel.getTable());
    }

    /**
     * Mueve al jugador 2 por (dx, dy). Solo funciona en modo PvP.
     *
     * @param dx delta horizontal
     * @param dy delta vertical
     */
    public void movePlayer2(int dx, int dy) {
        if (paused || gameOver || player2 == null) {
            return;
        }

        player2.move(dx, dy, currentLevel.getTable());
    }

    /**
     * Mueve al jugador maquina de forma aleatoria.
     */
    public void moveMachine() {
        if (mode != GameMode.PVM || player2 == null || paused || gameOver) {
            return;
        }

        ensureRandom();

        int[][] dirs = {
            {0, -1},  // arriba
            {0, 1},   // abajo
            {-1, 0},  // izquierda
            {1, 0},   // derecha
            {0, 0}    // quieto
        };

        int[] dir = dirs[random.nextInt(dirs.length)];
        player2.move(dir[0], dir[1], currentLevel.getTable());
    }

    /**
     * Verifica todas las colisiones del frame actual.
     * Incluye recoleccion de monedas, colision con enemigos,
     * elementos especiales y colision entre jugadores.
     */
    public void checkCollisions() {
        if (paused || gameOver) {
            return;
        }

        checkPlayerCollisions(player1);

        if (player2 != null) {
            checkPlayerCollisions(player2);

            if (player1.getCol() == player2.getCol() && player1.getRow() == player2.getRow()) {
                player1.hitByEnemy();
                player2.hitByEnemy();
            }
        }
    }

    /**
     * Verifica colisiones de un jugador especifico con monedas, enemigos,
     * zonas seguras y elementos especiales.
     *
     * @param player jugador a verificar
     */
    private void checkPlayerCollisions(Player player) {
        for (Coin coin : currentLevel.getCoins()) {
            if (coin.isAtPlayer(player)) {
                coin.collect(player);
            }
        }
        Zone zone = currentLevel.getTable().getZoneAt(player.getCol(), player.getRow());
        if (zone != null && zone.getType() == Zone.ZoneType.INTERMEDIATE) {
            player.setCheckpoint(player.getCol(), player.getRow());
        }
        for (Enemy enemy : currentLevel.getEnemies()) {
            if (enemy.getCol() == player.getCol() && enemy.getRow() == player.getRow()) {
                player.hitByEnemy();
                break;
            }
        }
        for (SpecialElement se : currentLevel.getSpecialElements()) {
            if (se.isActive() && se.getCol() == player.getCol() && se.getRow() == player.getRow()) {
                if (se.getType() != SpecialElement.ElementType.WALL) {
                    se.applyEffect(player);
                }
            }
        }
    }
    /**
     * Decrementa el temporizador en 1 segundo.
     *
     * @return true si el tiempo se agoto
     */
    public boolean tick() {
        if (paused || gameOver) {
            return false;
        }

        timeRemaining--;

        if (timeRemaining <= 0) {
            gameOver = true;
            return true;
        }

        return false;
    }

    /**
     * Verifica si algun jugador ha completado el nivel.
     *
     * @return true si el nivel esta completo
     */
    public boolean isLevelComplete() {
        if (isPlayerComplete(player1)) {
            return true;
        }

        if (player2 != null && isPlayerComplete(player2)) {
            return true;
        }

        return false;
    }

    /**
     * Verifica si un jugador individual ha completado el nivel,
     * todas las monedas recolectadas y en zona final.
     *
     * @param player jugador a verificar
     * @return true si completo el nivel
     */
    private boolean isPlayerComplete(Player player) {
        int totalCoins = currentLevel.getCoins().size();
        Zone zone = currentLevel.getTable().getZoneAt(player.getCol(), player.getRow());

        if (zone == null) {
            return false;
        }

        return player.getCoinsCollected() >= totalCoins &&
               zone.getType() == Zone.ZoneType.FINAL;
    }

    /**
     * Retorna el jugador que gano, o null.
     *
     * @return jugador ganador o null
     */
    public Player getWinner() {
        if (isPlayerComplete(player1)) {
            return player1;
        }

        if (player2 != null && isPlayerComplete(player2)) {
            return player2;
        }

        return null;
    }

    /**
     * Avanza al siguiente nivel.
     *
     * @return true si hay un nivel siguiente
     */
    public boolean nextLevel() {
        if (currentLevelIndex + 1 < levels.size()) {
            currentLevelIndex++;
            initLevel();
            return true;
        }

        return false;
    }

    /**
     * Retorna la cantidad de monedas recolectadas en el nivel actual.
     *
     * @return monedas recolectadas
     */
    public int coinsCollected() {
        int count = 0;

        for (Coin c : currentLevel.getCoins()) {
            if (c.isCollected()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Retorna el total de monedas en el nivel actual.
     *
     * @return total de monedas
     */
    public int totalCoins() {
        return currentLevel.getCoins().size();
    }

    /**
     * Alterna el estado de pausa del juego.
     */
    public void togglePause() {
        paused = !paused;
    }

    /**
     * Verifica si el juego esta pausado.
     *
     * @return true si esta pausado
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Guarda el estado completo del juego en un archivo usando serializacion Java.
     *
     * @param filename ruta al archivo de guardado .dat
     * @throws DOPOException si no se puede escribir el archivo
     */
    public void saveGame(String filename) throws DOPOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new DOPOException(DOPOException.SAVE_ERROR + e.getMessage());
        }
    }

    /**
     * Carga un estado de juego previamente guardado desde un archivo.
     *
     * @param filename ruta al archivo de guardado .dat
     * @return la instancia restaurada de worldHardestGame
     * @throws DOPOException si no se puede leer el archivo o esta corrupto
     */
    public static worldHardestGame loadGame(String filename) throws DOPOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            worldHardestGame loaded = (worldHardestGame) ois.readObject();
            loaded.ensureRandom();
            return loaded;
        } catch (IOException e) {
            throw new DOPOException(DOPOException.LOAD_ERROR + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new DOPOException(DOPOException.LOAD_ERROR + "Archivo incompatible: " + e.getMessage());
        }
    }

    /**
     * Asegura que el objeto Random exista.
     */
    private void ensureRandom() {
        if (random == null) {
            random = new Random();
        }
    }

    /** @return jugador 1 */
    public Player getPlayer1() {
        return player1;
    }

    /** @return jugador 2, null en modo un jugador */
    public Player getPlayer2() {
        return player2;
    }
    /** @return nivel actual */
    public Level getCurrentLevel() {
        return currentLevel;
    }
    /** @return numero del nivel actual */
    public int getCurrentLevelNum() {
        return currentLevelIndex + 1;
    }
    /** @return tiempo restante en segundos */
    public int getTimeRemaining() {
        return timeRemaining;
    }
    /** @return modalidad de juego */
    public GameMode getMode() {
        return mode;
    }
    /** @return true si el juego termino */
    public boolean isGameOver() {
        return gameOver;
    }
    /** @return posicion X del jugador 1 */
    public int getPlayerX() {
        return player1.getCol();
    }
    /** @return posicion Y del jugador 1 */
    public int getPlayerY() {
        return player1.getRow();
    }
    /** @return muertes del jugador 1 */
    public int getDeaths() {
        return player1.getDeaths();
    }
    /** @return filas del tablero */
    public int getRows() {
        return currentLevel.getTable().getRows();
    }
    /** @return columnas del tablero */
    public int getCols() {
        return currentLevel.getTable().getCols();
    }
}